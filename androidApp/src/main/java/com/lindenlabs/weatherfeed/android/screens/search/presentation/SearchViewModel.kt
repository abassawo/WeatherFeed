package com.lindenlabs.weatherfeed.android.screens.search.presentation

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.weatherfeed.android.data.Coordinate
import com.lindenlabs.weatherfeed.android.domain.GetLocation
import com.lindenlabs.weatherfeed.android.domain.OnLocationListener
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
    val getLocation: GetLocation,
    val recordSearchHistory: RecordSearchHistory,
) : ViewModel(), OnLocationListener {
    private val job = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val isFirstLaunch: Boolean = true
    val query = MutableStateFlow("")
    private val mutableViewState =
        MutableStateFlow(
            ViewState("", showPermissionNeeded = isLocationPermissionGranted())
        )
    var currentLocation: Location? = null
    val viewState: StateFlow<ViewState> = mutableViewState
    private val mutableViewEvent = MutableStateFlow<SearchScreenContract.ViewEvent?>(null)
    val viewEvent: StateFlow<SearchScreenContract.ViewEvent?> = mutableViewEvent

    init {
        if (isLocationPermissionGranted()) {
            getLocation(this)
        }
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
                showPermissionNeeded = true
            )
            if (isFirstLaunch) {
                mutableViewEvent.value = SearchScreenContract.ViewEvent.ShowLocationPermissionPrompt
            }
        }
    }

    private suspend fun emitViewState() {
        if (isLocationPermissionGranted()) {
            currentLocation?.let {
                val currentWeather = getSearchResultViewEntities.getWeather(it.toCoordinate())
                Log.d("SVM", "Handle interaction emit weather $currentWeather")

                currentWeather?.let {
                    mutableViewEvent.value =
                        SearchScreenContract.ViewEvent.ShowWeatherForCurrentLocation(it)
                }
            }
        }
    }

    fun handleInteraction(interaction: SearchScreenContract.PermissionInteraction) {
        Log.d("SVM", "Handle interactin")
        viewModelScope.launch {
            tryToEmitLiveWeatherUpdate()
        }
    }

    private fun SearchScreenContract.PermissionInteraction.isLocationEnabled(): Boolean {
        return this.permissions.any { it.value }
    }

    override fun onLocationAvailable(location: Location) {
        this.currentLocation = location
        ioScope.launch {
            emitViewState()
        }

    }

    override fun onLocationUnavailable() {
        this.currentLocation = null
        ioScope.launch {
            emitViewState()
        }
    }
}

private fun Location.toCoordinate(): Coordinate =
    Coordinate(this.latitude.toFloat(), this.longitude.toFloat())
