package com.lindenlabs.weatherfeed.android.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HistoryScreen() {
    val viewModel = hiltViewModel<HistoryViewModel>()

    when (val viewState = viewModel.viewState.collectAsState().value) {
        ViewState.Empty -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("No cities searched yet")
        }

        is ViewState.Error -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(viewState.message)
        }

        is ViewState.History -> LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
            items(viewState.previousQueries) {
                Text(it)
            }
        }
        ViewState.Loading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}