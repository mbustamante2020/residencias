package com.residencias.es.data.user


interface UserRepository {

    suspend fun getUser(accessToken: String?): User?

    suspend fun updateUser(accessToken: String?, user: User?): User?

}