package com.lindenlabs.weatherfeed.android.screens.search.presentation

import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse
import com.lindenlabs.weatherfeed.android.ui.WeatherCardViewEntity
import javax.inject.Inject

enum class UseCase {
    Search, LocationBased
}

class SearchViewMapper @Inject constructor() {

    fun map(response: RawWeatherResponse, useCase: UseCase = UseCase.Search): WeatherCardViewEntity {
        return WeatherCardViewEntity(
            query = response.cityName,
            description = response.description(useCase),
            rawWeatherResponse = response
        )
    }
}

fun RawWeatherResponse.description(useCase: UseCase): String {
    val suffix = when (useCase) {
        UseCase.Search -> cityName
        UseCase.LocationBased -> "your current location"
    }
    return "Feels like ${main.feelsLike} in $suffix"
}