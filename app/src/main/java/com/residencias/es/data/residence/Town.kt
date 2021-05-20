package com.residencias.es.data.residence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Town(
    @SerialName("id") val id: Int,
    @SerialName("municipio") var town: String? = null,
)

@Serializable
data class TownResponse(
    @SerialName("data") val data: List<Town>? = null,
)