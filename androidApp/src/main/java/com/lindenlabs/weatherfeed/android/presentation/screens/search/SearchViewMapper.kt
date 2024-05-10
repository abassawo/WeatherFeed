package com.lindenlabs.weatherfeed.android.presentation.screens.search

import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse
import com.lindenlabs.weatherfeed.android.presentation.ui.WeatherCardViewEntity
import javax.inject.Inject

sealed class UseCase {
    data class Search(val cityName: String) : UseCase()
    object Location : UseCase()
}

class SearchViewMapper @Inject constructor() {
    fun map(response: RawWeatherResponse, useCase: UseCase): WeatherCardViewEntity {
        val weatherIcon = response.main.weatherIcons.firstOrNull()
        return WeatherCardViewEntity(
            description = response.description(useCase),
            imageUrl = response.main.weatherIcons.firstOrNull()?.toUrl() ?: "",
            imageDescription = weatherIcon?.description ?: "",
            rawWeatherResponse = response
        )
    }
}

fun RawWeatherResponse.description(useCase: UseCase): String {
    val suffix = when (useCase) {
        is UseCase.Search -> "in ${useCase.cityName}"
        UseCase.Location -> "in your current location"
    }
    return "Feels like ${main.feelsLike} ºF $suffix"
}