package com.residencias.es

import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Before

abstract class ResidencesTest {

    protected val apiService = TestData.provideApiService(ApplicationProvider.getApplicationContext())

    @Before
    fun saveAccessToken() {
        // Launch Refresh Request
        runBlocking {
            TestData.setAccessToken(ApplicationProvider.getApplicationContext())
        }
    }
}