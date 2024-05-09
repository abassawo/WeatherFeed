package com.lindenlabs.weatherfeed.android.presentation.screens.geo_weather_card

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.lindenlabs.weatherfeed.android.BuildConfig
import com.lindenlabs.weatherfeed.android.MainActivity
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchScreenContract
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchViewModel


@Composable
fun CurrentWeatherCard(viewModel: SearchViewModel) {
    val viewState = viewModel.viewState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.refresh(SearchScreenContract.PermissionInteraction)
    }

    val showPermissionNeededCard = viewState.showPermissionNeeded

    AnimatedVisibility(visible = showPermissionNeededCard) {
        PermissionNeededCard()
    }

    AnimatedVisibility(visible = showPermissionNeededCard.not()) {
        Card(Modifier.fillMaxWidth()) {
            LaunchedEffect(Unit) {
                viewModel.refresh(SearchScreenContract.PermissionInteraction)
            }

            viewState.currentWeather?.let { viewEntity ->

                Box(contentAlignment = Alignment.TopCenter) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = viewEntity.description,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        AsyncImage(
                            model = viewEntity.imageUrl,
                            contentDescription = viewEntity.imageDescription,
                            modifier = Modifier.wrapContentSize()
                        )
                        Text(text = viewEntity.imageDescription)
                    }
                }
            }
        }
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