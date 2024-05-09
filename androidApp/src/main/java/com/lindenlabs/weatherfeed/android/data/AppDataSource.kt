package com.lindenlabs.weatherfeed.android.data

interface AppDataSource {
    suspend fun getWeatherForCoordinate(coordinate: Coordinate): RawWeatherResponse?
}