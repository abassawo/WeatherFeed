package com.lindenlabs.weatherfeed.android.domain.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.lindenlabs.weatherfeed.android.presentation.screens.search.IsLocationPermissionGranted
import javax.inject.Inject

interface OnLocationListener {
    fun onLocationAvailable(location: Location)
    fun onLocationUnavailable()
}

/*
    Interactor for use from within ViewModel - Used for edge case where
    the location permissions has just been enabled and the current weather data is not available
 */

class GetLocation @Inject constructor(
    val context: Context,
    val isLocationPermissionGranted: IsLocationPermissionGranted
) {

    operator fun invoke(listener: OnLocationListener) {
        if (isLocationPermissionGranted().not()) {
            return
        } else {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                val lastLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?: locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (lastLocation != null) {
                    listener.onLocationAvailable(lastLocation)
                } else {
                    listener.onLocationUnavailable()
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
                listener.onLocationUnavailable()
            }
        }
    }
}