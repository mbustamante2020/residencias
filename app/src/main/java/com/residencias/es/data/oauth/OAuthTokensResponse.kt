package com.residencias.es.data.oauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OAuthTokensResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("id") val id: String? = null,
    @SerialName("username") val name: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("role") val role: String? = null,
)

/*
            'access_token' => $token,
            'token_type' => 'bearer',
            'expires_in' => JWTAuth::factory()->getTTL() * 60,
            'username' => $name,
            'email' => $email,
            'role' => $role,
            'status' => 200


 */