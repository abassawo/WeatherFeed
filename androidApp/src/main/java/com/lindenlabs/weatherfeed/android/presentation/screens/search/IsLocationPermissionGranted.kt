package com.lindenlabs.weatherfeed.android.presentation.screens.search

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IsLocationPermissionGranted @Inject constructor(@ApplicationContext val context: Context) {

    operator fun invoke(): Boolean = hasPermission(ACCESS_FINE_LOCATION) ||
            hasPermission(ACCESS_COARSE_LOCATION)

     private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }
}
