package com.residencias.es.data.residence.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Province(
    @SerialName("id") val id: Int,
    @SerialName("provincia") var province: String? = null,
)

@Serializable
data class ProvinceResponse(
    @SerialName("data") val data: List<Province>? = null,
)