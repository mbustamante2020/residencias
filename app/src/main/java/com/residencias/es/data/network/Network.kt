package com.residencias.es.data.network

import android.content.Context
import android.util.Log
import com.residencias.es.data.oauth.Constants
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*

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
        }
    }

    private val json = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false

    }
}