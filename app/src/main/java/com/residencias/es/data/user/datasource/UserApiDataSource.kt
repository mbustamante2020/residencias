package com.residencias.es.data.user.datasource

import android.util.Log
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.user.User
import com.residencias.es.data.user.UserResponse
import io.ktor.client.*
import io.ktor.client.request.*

class UserApiDataSource( private val httpClient: HttpClient) {

    private val tag: String = "UserApiDataSource"

    /*********** PERFIL ************/
    // se obtienen los datos del usuario
    suspend fun getUser(): User? {
        return try {
            val response = httpClient.get<UserResponse>(Endpoints.urlUser)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    suspend fun updateUser(user: User?): User? {
        return try {
            val response = httpClient
                .put<UserResponse>(Endpoints.urlUser) {
                    user?.let {
                        parameter("username", it.userName)
                        parameter("name", it.name)
                        parameter("address", it.address)
                        parameter("phone", it.phone)
                    }
                }
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }
}