package com.residencias.es.data.oauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OAuthTokensResponse(
    @SerialName("token") val accessToken: String,
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("role") val role: String? = null,
)
