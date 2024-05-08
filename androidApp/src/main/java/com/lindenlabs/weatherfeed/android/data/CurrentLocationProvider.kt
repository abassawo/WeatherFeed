package com.lindenlabs.weatherfeed.android.data

object CurrentLocationProvider {

    operator fun invoke(): Coordinate = Coordinate(lat = 44.34f, lng = 10.99f)
}