package com.lindenlabs.weatherfeed.android

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.lindenlabs.weatherfeed.android.domain.location.GpsUtils
import com.lindenlabs.weatherfeed.android.domain.location.GpsUtils.Companion.LOCATION_REQUEST
import com.lindenlabs.weatherfeed.android.domain.location.LocationViewModel
import com.lindenlabs.weatherfeed.android.presentation.App
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: SearchViewModel by viewModels()
    val locationViewModel: LocationViewModel by viewModels()
    private var isGPSEnabled = false

    val locationPermissionRequest by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            this@MainActivity.isGPSEnabled = permissions.any { it.value }
            viewModel.refresh()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermission()
        setContentNavGraph()
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            listOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ).toTypedArray()
        )
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                this@MainActivity.isGPSEnabled = isGPSEnable
                viewModel.refresh()
            }
        })
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun setContentNavGraph() {
        setContent {
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> showLocationPermissionsDialog()

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> Toast.makeText(
                this,
                "App requires location to show current weather",
                Toast.LENGTH_SHORT
            ).show()

            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            ACCESS_COARSE_LOCATION
        )


    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
            it?.let {
                viewModel.refresh()
            }
        })
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED


    fun showLocationPermissionsDialog() {
        locationPermissionRequest.launch(
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            )
        )
    }
}


@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        App()
    }
}
