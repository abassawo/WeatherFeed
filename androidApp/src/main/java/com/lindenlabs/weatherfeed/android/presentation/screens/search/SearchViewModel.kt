package com.lindenlabs.weatherfeed.android.presentation.screens.search

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.weatherfeed.android.data.Coordinate
import com.lindenlabs.weatherfeed.android.domain.RecordSearchHistory
import com.lindenlabs.weatherfeed.android.domain.location.GetLocation
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
    val isLocationPermissionGranted: IsLocationPermissionGranted,
    val getLocation: GetLocation,
    val getSearchResultViewEntities: GetSearchResultViewEntities,
    val recordSearchHistory: RecordSearchHistory,
) : ViewModel() {
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
        val lastSearchedCity = recordSearchHistory.getHistory().lastOrNull()
        lastSearchedCity?.let {
            query.value = it
            mutableViewEvent.value = SearchScreenContract.ViewEvent.ShowLastSearch(it)
        }
    }

    fun search() {
        if (query.value.trim().isNotEmpty()) {
            recordSearchHistory(query.value.trim())
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
                currentWeather = getSearchResultViewEntities.getWeather(it)
            }
                ?: getLocation(object : OnLocationListener {
                    override fun onLocationAvailable(location: Location) {
                        ioScope.launch {
                            currentWeather =
                                location.let { getSearchResultViewEntities.getWeather(location.toCoordinate()) }
                        }
                    }

                    override fun onLocationUnavailable() {
                        currentWeather = null
                    }
                })
        }
        mutableViewState.value = viewState.copy(
            showPermissionNeeded = hasLocationPermissions.not(),
            currentWeather = currentWeather
        )
    }


    fun refresh(interaction: SearchScreenContract.PermissionInteraction = SearchScreenContract.PermissionInteraction) {
        Log.d("SVM", "Handle interaction $interaction")
        viewModelScope.launch {
            tryToEmitLiveWeatherUpdate()
        }
    }
}

private fun Location.toCoordinate(): Coordinate {
    return Coordinate(latitude.toFloat(), longitude.toFloat())
}
