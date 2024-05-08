package com.lindenlabs.weatherfeed.android.data

interface AppDataSource {
    suspend fun getWeather(city: String): RawWeatherResponse
    suspend fun getWeatherForCurrentLocation(): RawWeatherResponse
}