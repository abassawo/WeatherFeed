package com.lindenlabs.weatherfeed.android.data

import javax.inject.Inject

class WeatherlyLocationProvider @Inject constructor(){

    fun provideCurrentLocation(): Coordinate {
        return Coordinate(10.0f, 10.0f)
    }
}
