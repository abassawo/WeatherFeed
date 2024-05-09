package com.lindenlabs.weatherfeed.android.screens.search.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lindenlabs.weatherfeed.android.screens.geo_weather_card.CurrentWeatherCard
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchViewModel

@Composable
internal fun SearchScaffold(viewModel: SearchViewModel) {
    Scaffold(
        topBar = {
            CollapsingEffectScreen(viewModel)
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            SearchScreen(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingEffectScreen(viewModel: SearchViewModel) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .height(100.dp),
        lazyListState,
    ) {
        item {
            CenterAlignedTopAppBar(
                title = {
                    Text("Weather Feed")
                }
            )
        }
        item {
            CurrentWeatherCard(viewModel = viewModel)
        }
    }
}
