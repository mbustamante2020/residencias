package com.residencias.es

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class Test4 : ResidencesTest() {

    @Before
    fun initAccessToken() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            clearAccessToken()
        }
    }

    @After
    fun restoreCorrectAccessToken() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            clearAccessToken()
        }
    }

    @Test
    fun sessionManagerSavesOAuthTokens() {
        val initAccessToken = getAccessToken()

        //access_token se inicializa en initAccessToken() y se comprueba que sea null
        assert(initAccessToken == null)

        runBlocking {
            //se ingresa el correo y la clave para autenticarse
            //TestData.loginAccessToken(context)
            login()
            Thread.sleep(TestData.sharedPrefsWaitingMillis)
            val loginAccessToken = getAccessToken()


            //access_token no debe ser null
            assert(loginAccessToken !== null)
            Log.i("login access-token", getAccessToken().toString())

            //access_token se actualiza
            refreshToken()
            Thread.sleep(TestData.sharedPrefsWaitingMillis)
            val firstAccessToken = oauthLocalService.getAccessToken()

            //el nuevo access_token no debe ser null
            assert(firstAccessToken !== null)
            Log.i("first access-token", oauthLocalService.getAccessToken().toString())

            //finalmente loginAccessToken y refreshAccessToken deben ser distintos
            assert(loginAccessToken !== firstAccessToken)


            //access_token se actualiza
            refreshToken()

            Thread.sleep(TestData.sharedPrefsWaitingMillis)
            val secondAccessToken = oauthLocalService.getAccessToken()

            //el nuevo access_token no debe ser null
            assert(secondAccessToken !== null)
            Log.i("second access-token", oauthLocalService.getAccessToken().toString())

            //finalmente loginAccessToken y refreshAccessToken deben ser distintos
            assert(firstAccessToken !== secondAccessToken)

        }
    }
}