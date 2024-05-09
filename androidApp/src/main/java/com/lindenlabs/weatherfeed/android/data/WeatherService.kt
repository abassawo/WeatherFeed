package com.lindenlabs.weatherfeed.android.data

import com.lindenlabs.weatherfeed.android.utils.Config.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/geo/1.0/direct?")
    suspend fun getCoordinates(
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY
    ): List<Coordinate>

    @GET("/data/3.0/onecall?")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appid") apiKey: String = API_KEY
    ): RawWeatherResponse
}