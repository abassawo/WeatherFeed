package com.lindenlabs.weatherfeed.android

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

sealed class Screen(val title: String) {
    object FeedScreen : Screen("saved")
    object SearchScreen : Screen("search")

    data class Detail(val server: String, val photoId: String) :
        Screen("detail")
}

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        Screen.SearchScreen.title,
        Icons.Filled.Search,
        ""
    ),
    BottomNavigationItem(
        Screen.FeedScreen.title,
        Icons.Default.AddCircle,
        ""
    )
)