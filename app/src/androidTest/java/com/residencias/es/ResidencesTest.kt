package com.residencias.es

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.residencias.es.data.oauth.model.OAuthToken
import kotlinx.coroutines.delay

abstract class ResidencesTest {

    protected val oauthApiService = TestData.provideOauthApiService(ApplicationProvider.getApplicationContext())
    protected val oauthLocalService = TestData.provideOauthLocalService(ApplicationProvider.getApplicationContext())
    protected val residenceApiService = TestData.provideResidenceApiService(ApplicationProvider.getApplicationContext())

    suspend fun clearAccessToken() {
        oauthLocalService.clearAccessToken()
        oauthLocalService.clearRefreshToken()
        Log.i("clear access-token", oauthLocalService.getAccessToken().toString())
        delay(TestData.sharedPrefsWaitingMillis)
    }

    suspend fun login() {
        var oAuthToken: OAuthToken? = oauthApiService.login(TestData.email, TestData.password)
        oAuthToken?.accessToken?.let { oauthLocalService.saveAccessToken(it) }
    }

    suspend fun refreshToken() {
        var oAuthToken = oauthApiService.refresh(getAccessToken())
        oAuthToken?.accessToken?.let { oauthLocalService.saveAccessToken(it) }
    }

    fun getAccessToken(): String? {
        return oauthLocalService.getAccessToken()
    }
}