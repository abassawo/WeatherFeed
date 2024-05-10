package com.lindenlabs.weatherfeed.android.presentation.screens.search

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.weatherfeed.android.data.Coordinate
import com.lindenlabs.weatherfeed.android.domain.Memory
import com.lindenlabs.weatherfeed.android.domain.location.LocationInteractor
import com.lindenlabs.weatherfeed.android.domain.location.OnLocationListener
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchScreenContract.ViewState
import com.lindenlabs.weatherfeed.android.presentation.ui.WeatherCardViewEntity
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
    val isLocationPermissionGranted: LocationPermissions,
    val getLocation: LocationInteractor,
    val getSearchResultViewEntities: GetSearchResultViewEntities,
    val memory: Memory,
) : ViewModel(), OnLocationListener {
    private var currentWeather: WeatherCardViewEntity? = null
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
        val lastSearchedCity = memory.getHistory().lastOrNull()
        lastSearchedCity?.let {
            query.value = it
            mutableViewEvent.value = SearchScreenContract.ViewEvent.ShowLastSearch(it)
        }
    }

    fun search() {
        if (query.value.trim().isNotEmpty()) {
            memory(query.value.trim())
            ioScope.launch(ioScope.coroutineContext) {
                runCatching { getSearchResultViewEntities(query.value) }
                    .onSuccess {
                        Log.d("SVM", "Testing success $it")
                        mutableViewState.value =
                            viewState.value.copy(
                                showPermissionNeeded = isLocationPermissionGranted().not(),
                                citySearchResult = it,
                                isSearchActive = true
                            )
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
        val viewState = viewState.value.copy(
            showPermissionNeeded = isLocationPermissionGranted().not()
        )
        mutableViewState.value =
            viewState.copy(query = "", isSearchActive = false, citySearchResult = null)
    }

    private suspend fun tryToEmitLiveWeatherUpdate() {
        val hasLocationPermissions = isLocationPermissionGranted()
        if (hasLocationPermissions.not() && isFirstLaunch) {
            mutableViewEvent.value = SearchScreenContract.ViewEvent.ShowLocationPermissionPrompt
        }

        val viewState = viewState.value.copy(
            showPermissionNeeded = isLocationPermissionGranted().not()
        )
        if (hasLocationPermissions) {
            currentLocation?.let {
                currentWeather = getSearchResultViewEntities.getWeather(it, UseCase.Location)
            }
                ?: getLocation(this)
        }
        mutableViewState.value = viewState.copy(
            showPermissionNeeded = hasLocationPermissions.not(),
            currentWeather = currentWeather,
            query = query.value,
            citySearchResult = getSearchResultViewEntities.invoke(query.value),
            isSearchActive = query.value.trim().isNotEmpty()
        )
    }


    fun refresh() {
        viewModelScope.launch {
            tryToEmitLiveWeatherUpdate()
        }
    }

    override fun onLocationAvailable(location: Location) {
        ioScope.launch {
            currentWeather =
                location.let {
                    getSearchResultViewEntities.getWeather(
                        location.toCoordinate(),
                        UseCase.Location
                    )
                }
        }
    }

    override fun onLocationUnavailable() {
        currentWeather = null
    }
}

private fun Location.toCoordinate(): Coordinate {
    return Coordinate(latitude.toFloat(), longitude.toFloat())
}
