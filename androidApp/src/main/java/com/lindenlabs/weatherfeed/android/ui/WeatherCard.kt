package com.lindenlabs.weatherfeed.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WeatherCard(viewEntity: WeatherCardViewEntity?) {

    Card(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black)
    ) {
        Text(viewEntity?.description ?: "")
    }

}