package com.residencias.es.data.user

import com.residencias.es.data.user.datasource.UserApiDataSource

class UserRepositoryImpl(
    private val apiDataSource: UserApiDataSource
) : UserRepository {

    override suspend fun getUser(accessToken: String?): User? {
        return apiDataSource.getUser(accessToken)
    }

    override suspend fun updateUser(accessToken: String?, user: User?): User? {
        return apiDataSource.updateUser(accessToken, user)
    }
}