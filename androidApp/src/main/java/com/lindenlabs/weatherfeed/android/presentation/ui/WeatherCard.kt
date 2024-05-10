package com.lindenlabs.weatherfeed.android.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun WeatherCard(viewEntity: WeatherCardViewEntity?) {

    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Black)
    ) {
        Text(viewEntity?.description ?: "",
            Modifier
                .wrapContentSize()
                .padding(16.dp))
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            viewEntity?.imageUrl?.let {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = it,
                        contentDescription = viewEntity.imageDescription,
                        modifier = Modifier.size(200.dp)
                    )
                    Text(text = viewEntity?.imageDescription ?: "")
                }
            }
        }
    }
}