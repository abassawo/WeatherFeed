package com.lindenlabs.weatherfeed.android.navigation

import androidx.navigation.NavHostController
import com.lindenlabs.weatherfeed.android.Screen

class AppNavigator(private val navigator: NavHostController) {

    fun navigate(destination: Screen) {
        when(destination) {
            is Screen.Detail -> {
                val server = destination.server
                val photoId = destination.photoId
                val route = "detail/${server}/${photoId}"
                navigator.navigate(route)
            }
            else -> {
                val route = destination.title
                navigator.navigate(route)
            }
        }
    }

    fun navigateUp() = navigator.navigateUp()
}