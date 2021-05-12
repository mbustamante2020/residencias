package com.residencias.es.data.network

import android.content.Context
import android.util.Log
import com.residencias.es.data.datasource.SessionManager
import com.residencias.es.data.oauth.Constants
import com.residencias.es.data.oauth.OAuthTokensResponse
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

object Network {

    private const val TAG = "Network"

    fun createHttpClient(context: Context): HttpClient {
        return HttpClient(OkHttp) {

            engine {
                config {
                    retryOnConnectionFailure(true)
                }
            }


         //   install(WebSockets)



            // Json
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v(TAG, message)
                    }
                }
                //level = LogLevel.BODY // FAILS to submit
                //level = LogLevel.INFO // OK
                level = LogLevel.ALL
            }
            // Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L

            }
            // Apply to All Requests
            defaultRequest {
                // Content Type
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                // Client ID Header
                if (!headers.contains("client-id"))
                    header("client-id", Constants.clientID)
            }

            // Add OAuth Feature
            install(OAuthFeature) {
                getToken = {
                    val accessToken = SessionManager(context).getAccessToken() ?: ""
                    Log.d(TAG, "Adding Bearer header with token $accessToken")
                    accessToken
                }
                refreshToken = {
                    // Remove expired access token
                    SessionManager(context).clearAccessToken()
                    // Launch token refresh request
                    launchTokenRefresh(context)
                }
            }
        }
    }

    private val json = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true

    }

    private suspend fun launchTokenRefresh(context: Context) {
        val sessionManager = SessionManager(context)
        // Get Refresh Token
        sessionManager.getRefreshToken()?.let { refreshToken ->
            try {
                // Launch Refresh Request
                val response =
                        createHttpClient(context).post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                            parameter("token", refreshToken)
                            parameter("client_id", Constants.clientID)
                            parameter("client_secret", Constants.clientSecret)
                            //parameter("refresh_token", refreshToken)
                            //parameter("token", refreshToken)
                            parameter("grant_type", "refresh_token")
                        }
                Log.d(TAG, "Got new Access token ${response.accessToken}")
                // Save new Tokens
                sessionManager.saveAccessToken(response.accessToken)
                sessionManager.saveRefreshToken(response.accessToken)
            } catch (t: Throwable) {
                Log.d(TAG, "Error refreshing tokens", t)
                // Clear tokens
                sessionManager.clearAccessToken()
                sessionManager.clearRefreshToken()

            }
        } ?: run {
            Log.e(TAG, "No refresh token available")
            // Clear token
            sessionManager.clearAccessToken()
        }
    }
}