package com.lindenlabs.weatherfeed.android.data

import com.lindenlabs.weatherfeed.android.Config
import com.lindenlabs.weatherfeed.android.Config.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getWeatherForCity(
        @Query("q") city: String,
        @Query("appId") apiKey: String = API_KEY
    ): RawWeatherResponse

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appId") apiKey: String =  API_KEY
    ): RawWeatherResponse
}