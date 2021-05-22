package com.residencias.es

import android.content.Context
import android.util.Log
import com.residencias.es.data.datasource.ApiDataSource
import com.residencias.es.data.datasource.SessionManager
import com.residencias.es.data.network.Network
import com.residencias.es.data.oauth.OAuthTokensResponse
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay


object TestData {
    const val networkWaitingMillis = 5000L
    const val sharedPrefsWaitingMillis = 500L

    // Network
    fun provideHttpClient(context: Context): HttpClient = Network.createHttpClient(context)
    fun provideApiService(context: Context): ApiDataSource = ApiDataSource(provideHttpClient(context))

    // Tokens
    const val dummyAccessToken = "access_12345"
    const val dummyRefreshToken = "refresh_12345"
    const val refreshToken = "9my1jw1yczwvvrfcod6esdeacxyto41xz5wz4iimjrdwaq6pir"

    // User
    const val userName = "tareasuoc2020"
    const val userDescription = "Stream de Mario"
    const val updatedUserDescription = userDescription.plus("!")


    suspend fun clearAccessToken(context: Context) {
        SessionManager(context).clearAccessToken()
        SessionManager(context).clearRefreshToken()
        Log.i("clear access-token", SessionManager(context).getAccessToken().toString())
        delay(sharedPrefsWaitingMillis)
    }

    suspend fun setAccessToken(context: Context) {
        val response =
            provideHttpClient(context).post<OAuthTokensResponse>("https://residenciasysalud.es/api/auth/login") {
                // parameter("client_id", OAuthConstants.clientID)
                // parameter("client_secret", OAuthConstants.clientSecret)
                parameter("email", "mbustama1@uoc.edu")
                parameter("password", "")
            }
        // Save new access token
        SessionManager(context).saveAccessToken(response.accessToken)
        delay(sharedPrefsWaitingMillis)
    }

    // Token Refresh
    suspend fun loginAccessToken(context: Context) {
        val response =
            provideHttpClient(context).post<OAuthTokensResponse>("https://residenciasysalud.es/api/auth/login") {
                // parameter("client_id", OAuthConstants.clientID)
                // parameter("client_secret", OAuthConstants.clientSecret)
                parameter("email", "a@gmail.com")
                parameter("password", "parra21*")
            }
        // Save new access token
        SessionManager(context).saveAccessToken(response.accessToken)
        delay(sharedPrefsWaitingMillis)
    }


    suspend fun refreshAccessToken(context: Context, refreshToken1: String) {
        val response =
            provideHttpClient(context).post<OAuthTokensResponse>("https://residenciasysalud.es/api/auth/refresh") {
                // parameter("client_id", OAuthConstants.clientID)
                // parameter("client_secret", OAuthConstants.clientSecret)
                parameter("token", refreshToken1)
                //parameter("grant_type", "refresh_token")
            }
        // Save new access token
        SessionManager(context).saveAccessToken(response.accessToken)
        delay(sharedPrefsWaitingMillis)


    }
}