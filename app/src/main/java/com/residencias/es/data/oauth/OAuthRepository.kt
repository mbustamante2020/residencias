package com.residencias.es.data.oauth

import com.residencias.es.data.oauth.model.OAuthToken


interface OAuthRepository {

    suspend fun isUserAvailable(): Boolean

    suspend fun login(email: String, password: String): OAuthToken?

    suspend fun loginGoogle(email: String, name: String): OAuthToken?

    suspend fun register(name: String, email: String, password: String): OAuthToken?

    fun logout()

    fun onUnauthorized()

    fun saveAccessToken(accessToken: String)

    fun saveUserData(id: String?, name: String?, email: String?, role: String?)

    fun getAccessToken(): String?

    fun getId(): String?

    fun getName(): String?

    fun getEmail(): String?

    fun getRole(): String?
}