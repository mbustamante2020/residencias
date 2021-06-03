package com.residencias.es.data.oauth.datasource

import android.util.Log
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.oauth.model.OAuthToken
import com.residencias.es.utils.OAuthConstants
import io.ktor.client.*
import io.ktor.client.request.*

class OauthApiDataSource(private val httpClient: HttpClient )  {

    private val tag: String = "OauthApiDataSource"

    /*********** AUTENTICACIÓN ************/
    suspend fun login(email: String, password: String): OAuthToken? {
        return try {
            httpClient
                .post<OAuthToken>(Endpoints.urlAuthLogin) {
                    parameter("client_secret", OAuthConstants.clientSecret)
                    parameter("email", email)
                    parameter("password", password)
            }
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    suspend fun loginGoogle(email: String, name: String): OAuthToken? {
        return try {
            httpClient
                .post<OAuthToken>(Endpoints.urlAuthLoginGoogle) {
                    parameter("client_secret", OAuthConstants.clientSecret)
                    parameter("email", email)
                    parameter("name", name)
            }
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    suspend fun register(name: String, email: String, password: String): OAuthToken? {
        return try {
            httpClient
                .post<OAuthToken>(Endpoints.urlAuthRegister) {
                    parameter("client_secret", OAuthConstants.clientSecret)
                    parameter("name", name)
                    parameter("email", email)
                    parameter("password", password)
                    parameter("password_confirmation", password)
            }
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    suspend fun refresh(accessToken: String?): OAuthToken? {
        return try {
            httpClient
                .post<OAuthToken>(Endpoints.urlAuthRefresh) {
                    parameter("client_secret", OAuthConstants.clientSecret)
                    parameter("token", accessToken)
                }
        } catch (t: Throwable) {
            //Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

}