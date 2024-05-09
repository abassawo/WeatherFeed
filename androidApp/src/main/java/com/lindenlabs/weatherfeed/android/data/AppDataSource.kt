package com.lindenlabs.weatherfeed.android.data

interface AppDataSource {

    suspend fun getCoordinate(city: String) : Coordinate?
    suspend fun getWeatherForCoordinate(coordinate: Coordinate): RawWeatherResponse
}