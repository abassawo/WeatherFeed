package com.lindenlabs.weatherfeed.android.ui

import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun WeatherCard(viewEntity: WeatherCardViewEntity?) {
    LaunchedEffect(key1 = viewEntity) {
        if(viewEntity == null) {
            // Show dialog
        }

    }
    viewEntity?.let {
        Card(Modifier.background(Color.Black)) {
            Text(viewEntity.description)
        }
    }

}