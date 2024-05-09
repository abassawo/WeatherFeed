package com.lindenlabs.weatherfeed.android.domain.location

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(context: Context) : ViewModel() {

    private val locationData = LocationLiveData(context)

    fun getLocationData() = locationData
}