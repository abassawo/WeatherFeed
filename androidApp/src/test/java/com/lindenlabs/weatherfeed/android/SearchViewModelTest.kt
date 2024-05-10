package com.lindenlabs.weatherfeed.android

import com.lindenlabs.weatherfeed.android.data.AppDataSource
import com.lindenlabs.weatherfeed.android.data.Coordinate
import com.lindenlabs.weatherfeed.android.data.RawMainResponse
import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse
import com.lindenlabs.weatherfeed.android.data.WeatherIcon
import com.lindenlabs.weatherfeed.android.domain.Memory
import com.lindenlabs.weatherfeed.android.domain.location.LocationInteractor
import com.lindenlabs.weatherfeed.android.presentation.screens.search.GetSearchResultViewEntities
import com.lindenlabs.weatherfeed.android.presentation.screens.search.LocationPermissions
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchViewMapper
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchViewModel
import com.lindenlabs.weatherfeed.android.test.TestCoroutineRule
import com.lindenlabs.weatherfeed.android.test.whenever
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var viewModelScopeRule = TestCoroutineRule()
    private val mockLocationPermissions = mock(LocationPermissions::class.java)
    private val mockGetLocation: LocationInteractor = mock(LocationInteractor::class.java)
    private val mockDataSource = mock(AppDataSource::class.java)
    private val getSearchResultViewEntities =
        GetSearchResultViewEntities(mockDataSource, SearchViewMapper())
    private val mockRecordSearchHistory = mock(Memory::class.java)
    private val arrangeBuilder = ArrangeBuilder()
    private val testDispatcher = viewModelScopeRule.testCoroutineDispatcher
    private lateinit var underTest: SearchViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)

        underTest = SearchViewModel(
            mockLocationPermissions,
            mockGetLocation,
            getSearchResultViewEntities,
            mockRecordSearchHistory
        )
    }

    @Test
    fun `test that live weather is never emitted if location permissions are not granted`() {
        arrangeBuilder.withPermissions(false)
        verify(mockGetLocation, never()).invoke(underTest)
    }

    @Test
    fun `test that live weather is emitted if location permissions are granted`() {
        arrangeBuilder.withPermissions(true)
        underTest.refresh()
        verify(mockGetLocation).invoke(underTest)
    }

    @Test
    fun `test empty query yields no search result`() {
        underTest.apply {
            query.value = ""
            underTest.search()
        }
        val resultViewState = underTest.viewState
        assertNull(resultViewState.value.citySearchResult)
    }

    @Test
    fun `test non-empty query yields search result`() {
        testDispatcher.runBlockingTest {
            arrangeBuilder.withResult("New York")
        }

        underTest.apply {
            query.value = "New York"
            underTest.search()
        }
        val resultViewState = underTest.viewState
        assertNull(resultViewState.value.citySearchResult)
    }

    @Test
    fun `test non-empty search term is persisted`() {
        underTest.apply {
            query.value = "New York"
            underTest.search()
        }
        verify(mockRecordSearchHistory).invoke("New York")
    }


    inner class ArrangeBuilder {
        fun withPermissions(enabled: Boolean) = also {
            whenever(mockLocationPermissions.invoke()).thenReturn(enabled)
        }

        suspend fun withResult(query: String) = also {
            val coordinate = Coordinate(0f, 0f)
            whenever(mockDataSource.getCoordinate(query)).thenReturn(coordinate)
            whenever(mockDataSource.getWeatherForCoordinate(coordinate)).thenReturn(
                RawWeatherResponse(
                    "0f",
                    coordinate,
                    "Base",
                    RawMainResponse(
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        listOf(WeatherIcon("0r", "Main", "Hazy", "30D"))
                    )
                )
            )
        }
    }
}