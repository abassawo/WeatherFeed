package com.lindenlabs.weatherfeed.android.data

import javax.inject.Inject

class WeatherRepository @Inject constructor(val service: WeatherService, val locationProvider: WeatherlyLocationProvider) : AppDataSource {

    operator suspend fun invoke(city: String): RawWeatherResponse {
        return service.getWeatherForCity(city)
    }

    operator suspend fun invoke(): RawWeatherResponse {
        val currentLocation = locationProvider.provideCurrentLocation()
        return service.getWeather(currentLocation.lat.toString(), currentLocation.lng.toString())
    }

    override suspend fun getWeather(city: String): RawWeatherResponse {
       return this.invoke(city)
    }

    override suspend fun getWeatherForCurrentLocation(): RawWeatherResponse {
        return this.invoke()
    }

}