package com.lindenlabs.weatherfeed.android.data

import javax.inject.Inject

class WeatherRepository @Inject constructor(
    val service: WeatherService,
) : AppDataSource {

    operator suspend fun invoke(city: String): RawWeatherResponse {
        return service.getWeatherForCity(city)
    }

    override suspend fun getWeather(city: String): RawWeatherResponse {
        return this.invoke(city)
    }

    override suspend fun getWeatherForCoordinate(coordinate: Coordinate): RawWeatherResponse {
        return service.getWeather(coordinate.lat.toString(), coordinate.lat.toString())
    }

}