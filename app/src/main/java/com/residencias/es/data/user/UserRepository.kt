package com.residencias.es.data.user


interface UserRepository {

    suspend fun getUser(): User?

    suspend fun updateUser(user: User?): User?

}