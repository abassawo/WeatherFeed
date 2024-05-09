package com.lindenlabs.weatherfeed.android.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScaffold() {
    Scaffold(
        topBar = {
            val lazyListState = rememberLazyListState()
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
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
                    Text(text = "Recently Searched cities", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            HistoryScreen()
        }
    }
}