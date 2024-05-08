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
    private val externalScope = CoroutineScope(Dispatchers.Main)
    private val mutableViewState =
        MutableStateFlow<ViewState>(
            ViewState.Initial(showPermissionNeeded = isLocationPermissionGranted.invoke().not())
        )
    val viewState: StateFlow<ViewState> = mutableViewState

    private val mutableViewEvent = MutableStateFlow<SearchScreenContract.ViewEvent?>(null)
    val viewEvent: StateFlow<SearchScreenContract.ViewEvent?> = mutableViewEvent


    fun search(query: String) {
        if (query.isNotEmpty()) {
            recordSearchHistory(query)
            viewModelScope.launch(ioScope.coroutineContext) {
                runCatching { getSearchResultViewEntities(query) }
                    .onSuccess {
                        Log.d("SVM", "Testing success $it")
                        externalScope.launch {
                            mutableViewState.value =
                                (viewState.value as ViewState.Initial).copy(citySearchResult = it)
                        }
                    }
                    .onFailure {
                        Log.e("SVM", "Testing failed $it")
                    }
            }
        }
    }

    fun updateQuery(query: String) {
        mutableViewState.value = (mutableViewState.value as ViewState.Initial).copy(query = query)
    }

    private suspend fun tryToEmitLiveWeatherUpdate(interaction: SearchScreenContract.PermissionInteraction) {
        val hasLocationPermissions = interaction.isLocationEnabled()
        if (hasLocationPermissions) {
            val currentWeather = getSearchResultViewEntities.getWeatherForCurrentLocation()
            Log.d("SVM", "Handle interaction emit weather $currentWeather")


            externalScope.launch {
                mutableViewState.value = (viewState.value as ViewState.Initial).copy(
                    showPermissionNeeded = false,
                    currentWeather = currentWeather
                )
                currentWeather?.let {
                    mutableViewEvent.value =
                        SearchScreenContract.ViewEvent.ShowWeatherForCurrentLocation(it)
                }
            }

        } else {
            mutableViewState.value = (viewState.value as ViewState.Initial).copy(
                showPermissionNeeded = true
//                weatherForCurrentLocation = null
            )
            if (isFirstLaunch) {
                mutableViewEvent.value = SearchScreenContract.ViewEvent.ShowLocationPermissionPrompt
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