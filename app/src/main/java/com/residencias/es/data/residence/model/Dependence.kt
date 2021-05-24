package com.residencias.es.data.residence.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dependence(
    @SerialName("id") val id: Int,
    @SerialName("dependencia") var dependence: String? = null,
    @SerialName("residencias_id") var idResidence: Int? = null,
)

@Serializable
data class DependenceResponse(
    @SerialName("data") val data: List<Dependence>? = null,
)