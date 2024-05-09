package com.lindenlabs.weatherfeed.android.data

class LocalTestRepository : AppDataSource {
    override suspend fun getCoordinate(city: String): Coordinate? {
        return Coordinate(0f, 0f)
    }

    override suspend fun getWeatherForCoordinate(coordinate: Coordinate): RawWeatherResponse {
        return RawWeatherResponse(
            "id", Coordinate(0f, 0f), "base", RawMainResponse(
                temp = 400.0,
                feelsLike = 400.0,
                tempMin = 400.0,
                tempMax = 400.0,
                pressure = 400.0,
                humidity = 400.0,
                seaLevel = 400.0,
                groundLevel = 400.0,
                weatherIcons = listOf(WeatherIcon("721", "haze", "haze", "50d"))
            )
        )
    }
}