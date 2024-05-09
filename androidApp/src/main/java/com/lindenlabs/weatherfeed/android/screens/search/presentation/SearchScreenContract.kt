package com.lindenlabs.weatherfeed.android.screens.search.presentation

import com.lindenlabs.weatherfeed.android.ui.WeatherCardViewEntity

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

        data class ShowCurrentWeather(val viewEntity: WeatherCardViewEntity) : ViewEvent()

    }


    object PermissionInteraction
}