package com.residencias.es

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.residencias.es.data.oauth.datasource.SessionManager
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
            TestData.clearAccessToken(context)
        }
    }

    @After
    fun restoreCorrectAccessToken() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            //TestData.setAccessToken(context)
            //TestData.loginAccessToken(context)
            TestData.clearAccessToken(context)
        }
    }

    @Test
    fun sessionManagerSavesOAuthTokens() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val sessionManager = SessionManager(context)

        val initAccessToken = sessionManager.getAccessToken()

        //access_token se inicializa en initAccessToken() y se comprueba que sea null
        assert(initAccessToken == null)

        runBlocking {
            //se ingresa el correo y la clave para autenticarse
            TestData.loginAccessToken(context)
            Thread.sleep(TestData.sharedPrefsWaitingMillis)
            val loginAccessToken = sessionManager.getAccessToken()

            //access_token no debe ser null
            assert(loginAccessToken !== null)
            Log.i("login access-token", sessionManager.getAccessToken().toString())

            //access_token se actualiza
            TestData.refreshAccessToken(context, sessionManager.getAccessToken().toString())
            Thread.sleep(TestData.sharedPrefsWaitingMillis)
            val firstAccessToken = sessionManager.getAccessToken()

            //el nuevo access_token no debe ser null
            assert(firstAccessToken !== null)
            Log.i("first access-token", sessionManager.getAccessToken().toString())

            //finalmente loginAccessToken y refreshAccessToken deben ser distintos
            assert(loginAccessToken !== firstAccessToken)


            //access_token se actualiza
            TestData.refreshAccessToken(context, sessionManager.getAccessToken().toString())
            Thread.sleep(TestData.sharedPrefsWaitingMillis)
            val secondAccessToken = sessionManager.getAccessToken()

            //el nuevo access_token no debe ser null
            assert(secondAccessToken !== null)
            Log.i("second access-token", sessionManager.getAccessToken().toString())

            //finalmente loginAccessToken y refreshAccessToken deben ser distintos
            assert(firstAccessToken !== secondAccessToken)

        }
    }
}