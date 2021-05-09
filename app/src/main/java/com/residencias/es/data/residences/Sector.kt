package com.residencias.es.data.residences

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sector(
        @SerialName("id") val id: Int,
        @SerialName("plaza") var sector: String? = null,
        @SerialName("residencias_id") var idResidence: Int? = null,

        ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(sector)
        idResidence?.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sector> {
        override fun createFromParcel(parcel: Parcel): Sector {
            return Sector(parcel)
        }

        override fun newArray(size: Int): Array<Sector?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class SectorResponse(
    @SerialName("data") val data: List<Sector>? = null,
)