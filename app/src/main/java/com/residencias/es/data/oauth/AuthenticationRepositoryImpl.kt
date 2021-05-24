package com.residencias.es.data.oauth

import com.residencias.es.data.oauth.datasource.OauthApiDataSource
import com.residencias.es.data.oauth.datasource.SessionManager
import com.residencias.es.data.oauth.model.OAuthToken


class AuthenticationRepositoryImpl(
    private val apiDataSource: OauthApiDataSource,
    private val sharedPreferencesDataSource: SessionManager
) : AuthenticationRepository {

    override suspend fun isUserAvailable(): Boolean {
        return sharedPreferencesDataSource.isUserAvailable()
    }

    override suspend fun login(email: String, password: String): OAuthToken? {
        return apiDataSource.login(email, password)
    }

    override suspend fun loginGoogle(email: String, name: String): OAuthToken? {
        return apiDataSource.loginGoogle(email, name)
    }

    override suspend fun register(name: String, email: String, password: String): OAuthToken? {
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

    override fun saveAccessToken(accessToken: String) {
        sharedPreferencesDataSource.saveAccessToken(accessToken)
        sharedPreferencesDataSource.saveRefreshToken(accessToken)
    }

    override fun saveUserData(id: String?, name: String?, email: String?, role: String?) {
        sharedPreferencesDataSource.saveUserData(id, name, email, role)
    }

    override fun getAccessToken(): String? {
        return sharedPreferencesDataSource.getAccessToken()
    }

    override fun getId(): String? {
        return sharedPreferencesDataSource.getId()
    }

    override fun getName(): String? {
        return sharedPreferencesDataSource.getName()
    }

    override fun getEmail(): String? {
        return sharedPreferencesDataSource.getEmail()
    }

    override fun getRole(): String? {
        return sharedPreferencesDataSource.getRole()
    }
}