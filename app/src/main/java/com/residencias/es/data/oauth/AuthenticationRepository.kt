package com.residencias.es.data.oauth


interface AuthenticationRepository {

    suspend fun isUserAvailable(): Boolean

    // Returns true if the user logged in successfully. False otherwise
    suspend fun login(email: String, password: String): TokenResponse?

    suspend fun register(name: String, email: String, password: String): TokenResponse?

    fun logout()

    fun onUnauthorized()

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveUserData(id: String?, name: String?, email: String?)

    suspend fun getAccessToken(): String?
}