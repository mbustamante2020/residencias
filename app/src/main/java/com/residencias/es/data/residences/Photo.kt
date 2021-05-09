package com.residencias.es.data.residences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Photo(
        @SerialName("path") val id: String,
        @SerialName("file") val file: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (id != other.id) return false
        if (!file.contentEquals(other.file)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + file.contentHashCode()
        return result
    }
}

data class PhotoResponse(
        @SerialName("data") val data: Photo? = null,
)