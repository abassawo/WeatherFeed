package com.lindenlabs.weatherfeed.android.screens.search.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchScreenContract
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchViewModel
import com.lindenlabs.weatherfeed.android.ui.WeatherCard
import kotlinx.coroutines.flow.collect

@Composable
internal fun SearchScreen() {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val viewModel = hiltViewModel<SearchViewModel>()
            rememberSystemUiController().apply {
                setSystemBarsColor(
                    color = Color.Transparent
                )
            }
            val viewState = viewModel.viewState.collectAsState().value

            when (viewState) {
                is SearchScreenContract.ViewState.Initial -> {
                    with(viewState) {
                        Column(
                            modifier = Modifier
                                .background(Color.Blue)
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Row(Modifier.fillMaxSize()) {
                                TextField(
                                    value = query,
                                    onValueChange = { viewModel.updateQuery(it) })
                                Button(onClick = { viewModel.search(query) }) {
                                    Text("Search")
                                }
                            }

                            WeatherCard(viewState.citySearchResult)
                        }
                    }
                }
            }

        }
    }
}