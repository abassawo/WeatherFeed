package com.lindenlabs.weatherfeed.android

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lindenlabs.weatherfeed.android.domain.location.GpsUtils
import com.lindenlabs.weatherfeed.android.domain.location.GpsUtils.Companion.GPS_REQUEST
import com.lindenlabs.weatherfeed.android.domain.location.GpsUtils.Companion.LOCATION_REQUEST
import com.lindenlabs.weatherfeed.android.domain.location.LocationViewModel
import com.lindenlabs.weatherfeed.android.screens.history.HistoryScaffold
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchScreenContract
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchViewModel
import com.lindenlabs.weatherfeed.android.screens.search.presentation.views.SearchScaffold
import com.lindenlabs.weatherfeed.android.ui.components.BottomNavigation
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
            viewModel.refresh(SearchScreenContract.PermissionInteraction)
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
                viewModel.refresh(SearchScreenContract.PermissionInteraction)
            }
        })
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun setContentNavGraph() {
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { BottomNavigation(navController = navController) },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
            ) {

                val viewModel: SearchViewModel = hiltViewModel()
                NavHost(navController = navController, startDestination = "search") {
                    composable("search") {
                        SearchScaffold(viewModel)
                    }
                    composable("saved") {
                        HistoryScaffold()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "On Activity Result")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
            viewModel.refresh(SearchScreenContract.PermissionInteraction)
        }
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
//        GreetingView("Hello, Android!")
    }
}
