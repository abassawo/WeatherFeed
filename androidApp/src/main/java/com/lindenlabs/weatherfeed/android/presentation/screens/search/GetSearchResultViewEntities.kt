package com.lindenlabs.weatherfeed.android.presentation.screens.search

import com.lindenlabs.weatherfeed.android.data.AppDataSource
import com.lindenlabs.weatherfeed.android.data.Coordinate
import com.lindenlabs.weatherfeed.android.presentation.ui.WeatherCardViewEntity
import javax.inject.Inject

class GetSearchResultViewEntities @Inject constructor(
    private val repository: AppDataSource,
    private val viewMapper: SearchViewMapper,
) {
    suspend operator fun invoke(city: String): WeatherCardViewEntity? {
        return if (city.isNotEmpty()) {
            val coordinate = repository.getCoordinate(city)
            coordinate?.let {
                getWeather(it)
            }

        } else null
    }

    suspend fun getWeather(coordinate: Coordinate): WeatherCardViewEntity {
        val weatherResponse = repository.getWeatherForCoordinate(coordinate)
        return viewMapper.map(weatherResponse)
    }
}