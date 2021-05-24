package com.residencias.es.data.residence.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sector(
    @SerialName("id") val id: Int,
    @SerialName("plaza") var sector: String? = null,
    @SerialName("residencias_id") var idResidence: Int? = null,
)

@Serializable
data class SectorResponse(
    @SerialName("data") val data: List<Sector>? = null,
)