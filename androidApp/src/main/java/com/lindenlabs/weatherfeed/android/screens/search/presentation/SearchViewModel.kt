package com.lindenlabs.weatherfeed.android.screens.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.weatherfeed.android.domain.RecordSearchHistory
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchScreenContract.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val isLocationPermissionGranted: IsLocationPermissionGranted,
    val getSearchResultViewEntities: GetSearchResultViewEntities,
    val recordSearchHistory: RecordSearchHistory,
) : ViewModel() {
    private val job = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val isFirstLaunch: Boolean = true
    val query = MutableStateFlow("")
    private val mutableViewState =
        MutableStateFlow(
            ViewState("", showPermissionNeeded = isLocationPermissionGranted())
        )
    val viewState: StateFlow<ViewState> = mutableViewState

    private val mutableViewEvent = MutableStateFlow<SearchScreenContract.ViewEvent?>(null)
    val viewEvent: StateFlow<SearchScreenContract.ViewEvent?> = mutableViewEvent

    init {
        ioScope.launch {
            emitViewState()
        }
    }


    fun search() {
        if (query.value.isNotEmpty()) {
            recordSearchHistory(query.value)
            ioScope.launch(ioScope.coroutineContext) {
                runCatching { getSearchResultViewEntities(query.value) }
                    .onSuccess {
                        Log.d("SVM", "Testing success $it")
                        ioScope.launch {
                            mutableViewState.value =
                                viewState.value.copy(citySearchResult = it, isSearchActive = true)
                        }
                    }
                    .onFailure {
                        Log.e("SVM", "Testing failed $it")
                    }
            }
        }
    }

    fun updateQuery(query: String) {
        this.query.value = query
        if(query.trim().isEmpty()) {
            clearSearch()
        }
    }

    private fun clearSearch() {
        mutableViewState.value = viewState.value.copy(query = "", isSearchActive = false, citySearchResult = null)
    }

    private suspend fun tryToEmitLiveWeatherUpdate(interaction: SearchScreenContract.PermissionInteraction) {
        val hasLocationPermissions = interaction.isLocationEnabled()
        if (hasLocationPermissions) {
            ioScope.launch {
                emitViewState()
            }
        } else {
            mutableViewState.value = viewState.value.copy(
                showPermissionNeeded = true
            )
            if (isFirstLaunch) {
                mutableViewEvent.value = SearchScreenContract.ViewEvent.ShowLocationPermissionPrompt
            }
        }
    }

    private suspend fun emitViewState() {
        if (isLocationPermissionGranted()) {
            val currentWeather = getSearchResultViewEntities.getWeatherForCurrentLocation()
            Log.d("SVM", "Handle interaction emit weather $currentWeather")

            currentWeather?.let {
                mutableViewEvent.value =
                    SearchScreenContract.ViewEvent.ShowWeatherForCurrentLocation(it)
            }
        }
    }

    fun handleInteraction(interaction: SearchScreenContract.PermissionInteraction) {
        Log.d("SVM", "Handle interactin")
        viewModelScope.launch {
            tryToEmitLiveWeatherUpdate(interaction)
        }
    }


    private fun SearchScreenContract.PermissionInteraction.isLocationEnabled(): Boolean {
        return this.permissions.any { it.value }
    }
}