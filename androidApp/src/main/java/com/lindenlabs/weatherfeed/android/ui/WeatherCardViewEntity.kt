package com.lindenlabs.weatherfeed.android.ui

import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse


data class WeatherCardViewEntity(val query: String, val description: String, val rawWeatherResponse: RawWeatherResponse)


