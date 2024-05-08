package com.lindenlabs.weatherfeed.android.screens.geo_weather_card

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.lindenlabs.weatherfeed.android.BuildConfig
import com.lindenlabs.weatherfeed.android.MainActivity
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchScreenContract
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchViewModel


@Composable
fun CurrentWeatherCard() {
    val viewModel: SearchViewModel = hiltViewModel()
    when (val viewEvent = viewModel.viewEvent.collectAsState().value) {
        is SearchScreenContract.ViewEvent.ShowWeatherForCurrentLocation -> Text(text = viewEvent.currentWeather.description)
        SearchScreenContract.ViewEvent.ShowLocationPermissionPrompt -> PermissionNeededCard()
        else -> Unit
    }
}

@Composable
fun PermissionNeededCard() {
    val context = LocalContext.current as MainActivity
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Location service are not currently enabled")
            Button(onClick = {
                val i = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                )
                context.startActivity(i)
            }
            ) {
                Text(text = "Enable it now!")
            }
        }
    }
}