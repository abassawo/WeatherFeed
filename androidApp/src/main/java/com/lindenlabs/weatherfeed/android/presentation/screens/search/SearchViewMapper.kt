package com.lindenlabs.weatherfeed.android.presentation.screens.search

import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse
import com.lindenlabs.weatherfeed.android.presentation.ui.WeatherCardViewEntity
import javax.inject.Inject

class SearchViewMapper @Inject constructor() {
    fun map(response: RawWeatherResponse): WeatherCardViewEntity {
        val weatherIcon = response.main.weatherIcons.firstOrNull()
        return WeatherCardViewEntity(
            description = response.description(),
            imageUrl = response.main.weatherIcons.firstOrNull()?.toUrl() ?: "",
            imageDescription = weatherIcon?.description ?: "",
            rawWeatherResponse = response
        )
    }
}

fun RawWeatherResponse.description(): String {
    return "Feels like ${main.feelsLike}"
}