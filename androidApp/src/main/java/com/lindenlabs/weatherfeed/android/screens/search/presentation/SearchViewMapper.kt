package com.lindenlabs.weatherfeed.android.screens.search.presentation

import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse
import com.lindenlabs.weatherfeed.android.ui.WeatherCardViewEntity
import javax.inject.Inject

sealed class UseCase {
    data class Search(val query: String) : UseCase()
    object LocationBased : UseCase()
}

class SearchViewMapper @Inject constructor() {

    fun mapSearch(cityName: String, response: RawWeatherResponse): WeatherCardViewEntity {
        return WeatherCardViewEntity(
            description = response.description(UseCase.Search(cityName)),
            rawWeatherResponse = response
        )
    }

    fun mapCurrentWeather(response: RawWeatherResponse): WeatherCardViewEntity {
        return WeatherCardViewEntity(
//            query = response.cityName,
            description = response.description(UseCase.LocationBased),
            rawWeatherResponse = response
        )
    }
}

fun RawWeatherResponse.description(useCase: UseCase): String {
    val suffix = when (useCase) {
        is UseCase.Search -> useCase.query
        UseCase.LocationBased -> "your current location"
    }
    return "Feels like ${main.feelsLike} in $suffix"
}