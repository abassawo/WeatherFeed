package com.lindenlabs.weatherfeed.android.presentation.screens.history

import androidx.lifecycle.ViewModel
import com.lindenlabs.weatherfeed.android.domain.RecordSearchHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class ViewState {
    object Empty : ViewState()

    data class History(val previousQueries: List<String>) : ViewState()

    object Loading : ViewState()

    data class Error(val message: String) : ViewState()
}

@HiltViewModel
class HistoryViewModel @Inject constructor(val recordSearchHistory: RecordSearchHistory) :
    ViewModel() {
    private val mutableViewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: StateFlow<ViewState> = mutableViewState

    init {
        mutableViewState.value = ViewState.Loading
        runCatching {
            recordSearchHistory.getHistory().toList()
        }.onSuccess { previousQueries ->
            if (previousQueries.isEmpty()) {
                mutableViewState.value = ViewState.Empty
            } else {
                mutableViewState.value = ViewState.History(previousQueries)
            }
        }.onFailure {
            mutableViewState.value = ViewState.Error(it.message ?: "An error occurred")
        }
    }
}
