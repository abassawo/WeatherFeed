package com.lindenlabs.weatherfeed.android

import com.lindenlabs.weatherfeed.android.domain.Memory
import com.lindenlabs.weatherfeed.android.presentation.screens.history.HistoryViewModel
import com.lindenlabs.weatherfeed.android.presentation.screens.history.ViewState
import com.lindenlabs.weatherfeed.android.test.TestCoroutineRule
import com.lindenlabs.weatherfeed.android.test.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HistoryViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var viewModelScopeRule = TestCoroutineRule()
    private val mockMemory = mock(Memory::class.java)
    val arrangeBuilder = ArrangeBuilder()
    lateinit var underTest: HistoryViewModel
    private val testDispatcher = viewModelScopeRule.testCoroutineDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test empty state is emitted when no search history`() {
        arrangeBuilder.withEmptyState()
        underTest = HistoryViewModel(mockMemory)
        assertEquals(ViewState.Empty, underTest.viewState.value)
    }


    @Test
    fun `test history view state is emitted when there's search history`() {
        arrangeBuilder.withHistory()
        underTest = HistoryViewModel(mockMemory)
        assertEquals(ViewState.History(listOf("New York")), underTest.viewState.value)
    }

    inner class ArrangeBuilder {
        fun withEmptyState() = also {
            whenever(mockMemory.getHistory()).thenReturn(emptySet())
        }

        fun withHistory() = also {
            whenever(mockMemory.getHistory()).thenReturn(setOf("New York"))
        }
    }

}