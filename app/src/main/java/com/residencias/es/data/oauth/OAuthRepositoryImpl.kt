package com.residencias.es.data.oauth

import com.residencias.es.data.oauth.datasource.OauthApiDataSource
import com.residencias.es.data.oauth.datasource.SessionManager
import com.residencias.es.data.oauth.model.OAuthToken


class OAuthRepositoryImpl(
    private val apiDataSource: OauthApiDataSource,
    private val localDataSource: SessionManager
) : OAuthRepository {

    override suspend fun isUserAvailable(): Boolean {
        return localDataSource.isUserAvailable()
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
        localDataSource.cleaUserData()
        localDataSource.clearAccessToken()
    }

    override fun onUnauthorized() {
        localDataSource.cleaUserData()
        localDataSource.clearAccessToken()
    }

    override fun saveAccessToken(accessToken: String) {
        localDataSource.saveAccessToken(accessToken)
        localDataSource.saveRefreshToken(accessToken)
    }

    override fun saveUserData(id: String?, name: String?, email: String?, role: String?) {
        localDataSource.saveUserData(id, name, email, role)
    }

    override fun getAccessToken(): String? {
        return localDataSource.getAccessToken()
    }

    override fun getId(): String? {
        return localDataSource.getId()
    }

    override fun getName(): String? {
        return localDataSource.getName()
    }

    override fun getEmail(): String? {
        return localDataSource.getEmail()
    }

    override fun getRole(): String? {
        return localDataSource.getRole()
    }
}