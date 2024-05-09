package com.lindenlabs.weatherfeed.android.domain

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationRequest
import com.lindenlabs.weatherfeed.android.screens.search.presentation.IsLocationPermissionGranted
import javax.inject.Inject

interface OnLocationListener {
    fun onLocationAvailable(location: Location)
    fun onLocationUnavailable()
}

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
//                locationManager.g
//                val lastLocation =
//                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                        ?: locationManager.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                if (lastLocation != null) {
//                    listener.onLocationAvailable(lastLocation)
//                } else {
//                    listener.onLocationUnavailable()
//                }
            } catch (e: SecurityException) {
                e.printStackTrace()
                listener.onLocationUnavailable()
            }
        }
    }
}