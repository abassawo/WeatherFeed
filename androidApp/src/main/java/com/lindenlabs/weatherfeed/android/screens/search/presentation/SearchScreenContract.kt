package com.lindenlabs.weatherfeed.android.screens.search.presentation

import com.lindenlabs.weatherfeed.android.ui.WeatherCardViewEntity

object SearchScreenContract {

    sealed class ViewState {

        data class Initial(
            val showPermissionNeeded: Boolean,
            val query: String = "",
            val citySearchResult: WeatherCardViewEntity? = null,
            val currentWeather: WeatherCardViewEntity? = null,
            val isSearchActive: Boolean = false
        ) : ViewState()
    }

    sealed class ViewEvent {
        object ShowLocationPermissionPrompt : ViewEvent()

        data class ShowWeatherForCurrentLocation(val currentWeather: WeatherCardViewEntity) : ViewEvent()
    }


    data class PermissionInteraction(val permissions: Map<String, Boolean>)
}