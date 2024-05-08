package com.lindenlabs.weatherfeed.android.data

import com.google.gson.annotations.SerializedName

data class RawWeatherResponse(
    val id: String,
    @SerializedName("name") val cityName: String,
    val coord: Coordinate,
    val rawWeathers: List<RawWeather>,
    val base: String,
    val main: RawMainResponse,
)

data class RawWeather(val id: Int, val main: String, val description: String, val icon: String)

data class RawMainResponse(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Double,
    val humidity: Double,
    @SerializedName("sea_level") val seaLevel: Double,
    @SerializedName("grnd_level") val groundLevel: Double
)


data class Coordinate(val lat: Float, val lng: Float )