package com.residencias.es.data.residences

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Town(
        @SerialName("id") val id: Int,
        @SerialName("municipio") var town: String? = null,

        ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(town)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Town> {
        override fun createFromParcel(parcel: Parcel): Town {
            return Town(parcel)
        }

        override fun newArray(size: Int): Array<Town?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class TownResponse(
    @SerialName("data") val data: List<Town>? = null,
)