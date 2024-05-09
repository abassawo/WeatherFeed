package com.lindenlabs.weatherfeed.android.data

import javax.inject.Inject

class WeatherRepository @Inject constructor(
    val service: WeatherService,
) : AppDataSource {

    operator suspend fun invoke(city: String): RawWeatherResponse? {
        val coordinate = service.getCoordinates(city).firstOrNull()
        return coordinate?.let { getWeatherForCoordinate(coordinate) }
    }

    override suspend fun getCoordinate(city: String): Coordinate? {
        return service.getCoordinates(city).firstOrNull()
    }

    override suspend fun getWeatherForCoordinate(coordinate: Coordinate): RawWeatherResponse {
        return service.getWeather(coordinate.lat.toString(), coordinate.lng.toString())
    }

}