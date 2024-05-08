package com.lindenlabs.weatherfeed.android.screens.search.domain

import com.lindenlabs.weatherfeed.android.data.AppDataSource
import com.lindenlabs.weatherfeed.android.screens.search.presentation.UseCase
import com.lindenlabs.weatherfeed.android.screens.search.presentation.description
import com.lindenlabs.weatherfeed.android.ui.WeatherCardViewEntity
import javax.inject.Inject

interface SearchScreenInteractor {
    suspend operator fun invoke(query: String): WeatherCardViewEntity
}

class GetSearchScreenUi @Inject constructor(private val appDataSource: AppDataSource) :
    SearchScreenInteractor {

    override suspend fun invoke(query: String): WeatherCardViewEntity {
        val rawResponse = appDataSource.getWeather(query)
        return WeatherCardViewEntity(query, rawResponse.description(UseCase.Search), rawResponse)

    }
}