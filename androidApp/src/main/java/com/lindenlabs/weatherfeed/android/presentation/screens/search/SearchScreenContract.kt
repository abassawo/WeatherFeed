package com.lindenlabs.weatherfeed.android.presentation.screens.search

import com.lindenlabs.weatherfeed.android.presentation.ui.WeatherCardViewEntity

object SearchScreenContract {

    data class ViewState(
        val query: String = "",
        val showPermissionNeeded: Boolean,
        val currentWeather: WeatherCardViewEntity? = null,
        val citySearchResult: WeatherCardViewEntity? = null,
        val isSearchActive: Boolean = false
    )


    sealed class ViewEvent {
        object ShowLocationPermissionPrompt : ViewEvent()

        data class ShowLastSearch(val city: String) : ViewEvent()

    }

    object PermissionInteraction
}