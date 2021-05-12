package com.residencias.es.data.oauth


interface AuthenticationRepository {

    suspend fun isUserAvailable(): Boolean

    suspend fun login(email: String, password: String): OAuthTokensResponse?

    suspend fun loginGoogle(email: String, name: String): OAuthTokensResponse?

    suspend fun register(name: String, email: String, password: String): OAuthTokensResponse?

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