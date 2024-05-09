package com.lindenlabs.weatherfeed.android.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lindenlabs.weatherfeed.android.presentation.screens.history.HistoryScaffold
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchViewModel
import com.lindenlabs.weatherfeed.android.presentation.screens.search.views.SearchScaffold
import com.lindenlabs.weatherfeed.android.presentation.ui.components.BottomNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) {

        val viewModel: SearchViewModel = hiltViewModel()
        NavHost(navController = navController, startDestination = "search") {
            composable("search") {
                SearchScaffold(viewModel)
            }
            composable("saved") {
                HistoryScaffold()
            }
        }
    }
}