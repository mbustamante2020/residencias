package com.residencias.es.data.residence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    @SerialName("id") val id: Int,
    @SerialName("habitacion") var room: String? = null,
    @SerialName("residencias_id") var idResidence: Int? = null,
)

@Serializable
data class RoomResponse(
    @SerialName("data") val data: List<Room>? = null,
)