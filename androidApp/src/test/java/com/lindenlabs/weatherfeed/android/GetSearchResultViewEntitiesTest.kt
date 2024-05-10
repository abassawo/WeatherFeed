package com.lindenlabs.weatherfeed.android

import com.lindenlabs.weatherfeed.android.data.AppDataSource
import com.lindenlabs.weatherfeed.android.data.Coordinate
import com.lindenlabs.weatherfeed.android.data.RawMainResponse
import com.lindenlabs.weatherfeed.android.data.RawWeatherResponse
import com.lindenlabs.weatherfeed.android.data.WeatherIcon
import com.lindenlabs.weatherfeed.android.presentation.screens.search.GetSearchResultViewEntities
import com.lindenlabs.weatherfeed.android.presentation.screens.search.SearchViewMapper
import com.lindenlabs.weatherfeed.android.presentation.ui.WeatherCardViewEntity
import com.lindenlabs.weatherfeed.android.test.TestCoroutineRule
import com.lindenlabs.weatherfeed.android.test.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class GetSearchResultViewEntitiesTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var viewModelScopeRule = TestCoroutineRule()
    val mockRepository = mock(AppDataSource::class.java)
    val searchViewMapper = SearchViewMapper()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher by lazy { viewModelScopeRule.testCoroutineDispatcher }
    private val arrangeBuilder = ArrangeBuilder()


    val underTest: GetSearchResultViewEntities =
        GetSearchResultViewEntities(mockRepository, searchViewMapper)

    @Test
    fun `test empty city search yields null response`() {
        testDispatcher.runBlockingTest {
            val actual = underTest.invoke("")
            val expected = null
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test non-empty city search yields View Entity result`() {
        testDispatcher.runBlockingTest {
            arrangeBuilder.withResponse("New York")
            val actual = underTest.invoke("New York")
            assert(actual is WeatherCardViewEntity)
            assertEquals("Feels like 0.0 ºF in New York", actual!!.description)
        }
    }

    inner class ArrangeBuilder {
        suspend fun withResponse(city: String) = also {
            val coordinate = Coordinate(0f, 0f)
            whenever(mockRepository.getCoordinate(city)).thenReturn(coordinate)
            whenever(mockRepository.getWeatherForCoordinate(coordinate)).thenReturn(
                RawWeatherResponse(
                    "0f",
                    coordinate,
                    "Base",
                    RawMainResponse(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        listOf(WeatherIcon("0r", "Main", "Hazy", "30D")))
                )
            )
        }
    }
}