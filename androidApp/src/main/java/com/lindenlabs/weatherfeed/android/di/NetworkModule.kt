package com.lindenlabs.weatherfeed.android.di

import com.lindenlabs.weatherfeed.android.data.AppDataSource
import com.lindenlabs.weatherfeed.android.data.LocalTestRepository
import com.lindenlabs.weatherfeed.android.data.WeatherRepository
import com.lindenlabs.weatherfeed.android.data.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideAppDataSource(service: WeatherService): AppDataSource = LocalTestRepository()
//        WeatherRepository(service)

}