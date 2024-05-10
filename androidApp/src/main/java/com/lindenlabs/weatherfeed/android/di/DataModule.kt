package com.lindenlabs.weatherfeed.android.di

import android.content.Context
import android.content.SharedPreferences
import com.lindenlabs.weatherfeed.android.data.WeatherService
import com.lindenlabs.weatherfeed.android.domain.Memory
import com.lindenlabs.weatherfeed.android.domain.RecordSearchHistory
import com.lindenlabs.weatherfeed.android.domain.location.GetLocation
import com.lindenlabs.weatherfeed.android.domain.location.LocationInteractor
import com.lindenlabs.weatherfeed.android.presentation.screens.search.IsLocationPermissionGranted
import com.lindenlabs.weatherfeed.android.presentation.screens.search.LocationPermissions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    fun provideLocationPermissions(context: Context): LocationPermissions =
        IsLocationPermissionGranted(context)

    @Provides
    fun provideLocationInteractor(context: Context, locationPermissions: LocationPermissions): LocationInteractor =
        GetLocation(context, locationPermissions)

    @Provides
    fun provideMemory(sharedPreferences: SharedPreferences): Memory =
        RecordSearchHistory(sharedPreferences)

    @Provides
    fun provideWeatherApi(): WeatherService {
        return weatherApi
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("history", Context.MODE_PRIVATE)


    val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherService::class.java)

}