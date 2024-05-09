package com.lindenlabs.weatherfeed.android.domain

import android.content.SharedPreferences
import javax.inject.Inject

class RecordSearchHistory @Inject constructor(private val sharedPreferences: SharedPreferences) {

    operator fun invoke(query: String) {
        if (query.trim().isNotEmpty()) {
            val history = getHistory().toMutableSet()
            history.add(query)
            sharedPreferences
                .edit()
                .putStringSet("history", history)
                .apply()
        }
    }

    fun getHistory(): Set<String> =
        sharedPreferences.getStringSet("history", emptySet()) ?: emptySet()

}