package com.lindenlabs.weatherfeed.android.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun <T> `whenever`(methodCall: T): OngoingStubbing<T> {
    return Mockito.`when`(methodCall)
}

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestWatcher() {
    val testCoroutineDispatcher = TestCoroutineDispatcher()

    override fun starting(description: Description?) {
        super.starting(description)
        kotlinx.coroutines.test.runTest {

        }
    }


    override fun finished(description: Description?) {
        super.finished(description)
    }
}