package com.lindenlabs.weatherfeed.android.screens.search.presentation

import com.lindenlabs.weatherfeed.android.data.Coordinate
import com.lindenlabs.weatherfeed.android.data.WeatherRepository
import com.lindenlabs.weatherfeed.android.ui.WeatherCardViewEntity
import javax.inject.Inject

class GetSearchResultViewEntities @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val viewMapper: SearchViewMapper,
) {
    suspend operator fun invoke(city: String): WeatherCardViewEntity? {
        return if (city.isNotEmpty()) {
            weatherRepository.invoke(city)?.let { viewMapper.mapSearch(city, it) }
        } else null
    }

    suspend fun getWeather(coordinate: Coordinate): WeatherCardViewEntity {
        val weatherResponse = weatherRepository.getWeatherForCoordinate(coordinate)
        return viewMapper.mapCurrentWeather(weatherResponse)
    }
}