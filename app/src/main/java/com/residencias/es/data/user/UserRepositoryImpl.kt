package com.residencias.es.data.user

import com.residencias.es.data.user.datasource.UserApiDataSource

class UserRepositoryImpl(
    private val apiDataSource: UserApiDataSource
) : UserRepository {

    override suspend fun getUser(): User? {
        return apiDataSource.getUser()
    }

    override suspend fun updateUser(user: User?): User? {
        return apiDataSource.updateUser(user)
    }
}