package com.lindenlabs.weatherfeed.android.domain

import android.content.SharedPreferences
import javax.inject.Inject

interface Memory {
    operator fun invoke(query: String)
    fun getHistory(): Set<String>
}

class RecordSearchHistory @Inject constructor(private val sharedPreferences: SharedPreferences) :
    Memory {

    override operator fun invoke(query: String) {
        if (query.trim().isNotEmpty()) {
            val history = getHistory().toMutableSet()
            history.add(query)
            sharedPreferences
                .edit()
                .putStringSet("history", history)
                .apply()
        }
    }

    override fun getHistory(): Set<String> =
        sharedPreferences.getStringSet("history", emptySet()) ?: emptySet()

}