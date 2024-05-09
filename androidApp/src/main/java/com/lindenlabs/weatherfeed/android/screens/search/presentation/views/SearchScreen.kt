package com.lindenlabs.weatherfeed.android.screens.search.presentation.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchScreenContract
import com.lindenlabs.weatherfeed.android.screens.search.presentation.SearchViewModel
import com.lindenlabs.weatherfeed.android.ui.WeatherCard

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(viewModel: SearchViewModel) {
    val viewState = viewModel.viewState.collectAsState().value
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            rememberSystemUiController().apply {
                setSystemBarsColor(
                    color = Color.Transparent
                )
            }
            val queryText = viewModel.query.collectAsState().value
            with(viewState) {
                val keyboard = LocalSoftwareKeyboardController.current
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        SearchBar(
                            query = queryText,
                            placeholder = { Text("Search for a city") },
                            onQueryChange = { viewModel.updateQuery(it) },
                            onSearch = {
                                viewModel.search()
                                keyboard?.hide()
                            },
                            active = isSearchActive,
                            onActiveChange = {},
                            modifier = Modifier
                                .wrapContentHeight()
                                .onKeyEvent {
                                    return@onKeyEvent when {
                                        (it.nativeKeyEvent.keyCode) == 66 -> {
                                            viewModel.search()
                                            keyboard?.hide()
                                            true
                                        }

                                        else -> false
                                    }
                                }
                                .fillMaxWidth()
                                .padding(16.dp),
                            enabled = true
                        ) {
                            AnimatedVisibility(visible = viewState.isSearchActive) {
                                WeatherCard(viewEntity = viewState.citySearchResult)
                            }
                        }
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(onClick = {

                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close search"
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = !isSearchActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(onClick = {

                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Open search"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}