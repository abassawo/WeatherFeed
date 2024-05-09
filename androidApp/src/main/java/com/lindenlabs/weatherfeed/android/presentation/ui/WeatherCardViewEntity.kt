package com.lindenlabs.weatherfeed.android.presentation.ui

import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse


data class WeatherCardViewEntity(val description: String, val imageUrl: String, val imageDescription: String, val rawWeatherResponse: RawWeatherResponse)


