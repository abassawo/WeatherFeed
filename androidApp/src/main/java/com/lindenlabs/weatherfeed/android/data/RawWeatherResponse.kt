package com.lindenlabs.weatherfeed.android.data

import com.google.gson.annotations.SerializedName

data class RawWeatherResponse(
    val id: String,
    val coord: Coordinate,
    val base: String,
    @SerializedName("current") val main: RawMainResponse,
)


data class RawMainResponse(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Double,
    val humidity: Double,
    @SerializedName("sea_level") val seaLevel: Double,
    @SerializedName("grnd_level") val groundLevel: Double,
    @SerializedName("weather") val weatherIcons: List<WeatherIcon>
)

data class WeatherIcon(val id: String, val main: String, val description: String, val icon: String) {
    fun toUrl(): String {
        return "https://openweathermap.org/img/wn/$icon@2x.png"
    }
}


data class Coordinate(val lat: Float, @SerializedName("lon") val lng: Float)