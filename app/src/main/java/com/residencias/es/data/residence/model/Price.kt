package com.residencias.es.data.residence.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Price(
    @SerialName("id") val id: Int,
    @SerialName("precio") var price: String? = null,
)

@Serializable
data class PriceResponse(
    @SerialName("data") val data: List<Price>? = null,
)