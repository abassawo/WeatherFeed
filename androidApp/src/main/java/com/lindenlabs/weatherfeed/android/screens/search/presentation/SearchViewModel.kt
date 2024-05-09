package com.lindenlabs.weatherfeed.android.screens.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.weatherfeed.android.data.Coordinate
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
    var currentLocation: Coordinate? = null
    val viewState: StateFlow<ViewState> = mutableViewState
    private val mutableViewEvent = MutableStateFlow<SearchScreenContract.ViewEvent?>(null)
    val viewEvent: StateFlow<SearchScreenContract.ViewEvent?> = mutableViewEvent
    init {
        ioScope.launch {
            tryToEmitLiveWeatherUpdate()
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
        if (query.trim().isEmpty()) {
            clearSearch()
        }
    }

    private fun clearSearch() {
        mutableViewState.value =
            viewState.value.copy(query = "", isSearchActive = false, citySearchResult = null)
    }

    private suspend fun tryToEmitLiveWeatherUpdate() {
        val hasLocationPermissions = isLocationPermissionGranted()
        if (hasLocationPermissions) {
            ioScope.launch {
                emitViewState()
            }
        } else {
            mutableViewState.value = viewState.value.copy(
                showPermissionNeeded = true,
                currentWeather = null
            )
            if (isFirstLaunch) {
                mutableViewEvent.value = SearchScreenContract.ViewEvent.ShowLocationPermissionPrompt
            }
        }
    }

    private suspend fun emitViewState() {
        if (isLocationPermissionGranted()) {
            currentLocation?.let { coordinate ->
                val currentWeather = getSearchResultViewEntities.getWeather(coordinate)
                Log.d("SVM", "Handle interaction emit weather $currentWeather")
            }
        }
    }

    fun handleInteraction(interaction: SearchScreenContract.PermissionInteraction) {
        Log.d("SVM", "Handle interaction")
        val isGpsEnabled = interaction.isGpsEnabled
        mutableViewState.value = viewState.value.copy(showPermissionNeeded = isGpsEnabled.not())
        viewModelScope.launch {
            tryToEmitLiveWeatherUpdate()
        }
    }

    fun updateCurrentLocation(coordinate: Coordinate?) {
        this.currentLocation = coordinate
        ioScope.launch {
            currentLocation?.let {
                val currentWeather = getSearchResultViewEntities.getWeather(it)
                CoroutineScope(Dispatchers.Main).launch {
                    mutableViewState.value = viewState.value.copy(currentWeather = currentWeather)
                }
            }
        }

        Log.d("SVM", "Location retrieved $currentLocation")
    }
}