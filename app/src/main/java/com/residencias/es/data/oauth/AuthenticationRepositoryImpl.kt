package com.residencias.es.data.oauth

import com.residencias.es.data.SessionManager
import com.residencias.es.data.datasource.ApiDataSource


class AuthenticationRepositoryImpl(
        private val apiDataSource: ApiDataSource,
        private val sharedPreferencesDataSource: SessionManager
) : AuthenticationRepository {

    override suspend fun isUserAvailable(): Boolean {
        return sharedPreferencesDataSource.isUserAvailable()
    }

    override suspend fun login(email: String, password: String): TokenResponse? {
        return apiDataSource.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): TokenResponse? {
        return apiDataSource.register(name, email, password)
    }

    override fun logout() {
        sharedPreferencesDataSource.cleaUserData()
        sharedPreferencesDataSource.clearAccessToken()
    }

    override fun onUnauthorized() {
        sharedPreferencesDataSource.cleaUserData()
        sharedPreferencesDataSource.clearAccessToken()
    }

    override suspend fun saveAccessToken(accessToken: String) {
        sharedPreferencesDataSource.saveAccessToken(accessToken)
    }

    override suspend fun saveUserData(id: String?, name: String?, email: String?) {
        sharedPreferencesDataSource.saveUserData(id, name, email)
    }

    override suspend fun getAccessToken(): String? {
        return sharedPreferencesDataSource.getAccessToken()
    }
}