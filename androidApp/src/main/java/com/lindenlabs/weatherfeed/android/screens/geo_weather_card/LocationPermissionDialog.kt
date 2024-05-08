package com.lindenlabs.weatherfeed.android.screens.geo_weather_card


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.lindenlabs.weatherfeed.android.R

@Composable
fun LocationPermissionDialog(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onCancel: () -> Unit
) {
    var showDialog = remember { mutableStateOf(false) }
//
//    val requestLocationPermission = {
//        showDialog.value = true
//    }
//
    val dismissDialog = {
        showDialog.value = false
    }

    AnimatedVisibility(visible = showDialog.value) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = { Text(stringResource(id = R.string.location_permission_title)) },
            text = {
                Column {
                    Text(
                        stringResource(id = R.string.location_permission_subtitle)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onPermissionGranted()
                    dismissDialog()
                }) {
                    Text(stringResource(id = R.string.accept))
                }

            },
            dismissButton = {
                Button(onClick = {
                    onPermissionDenied()
                    dismissDialog()
                }) {
                    Text(stringResource(id = R.string.deny))
                }
            }
        )
    }

//    LaunchedEffect(key1 = Unit) {
//        requestLocationPermission()
//    }

}
