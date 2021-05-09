package com.residencias.es.data.residences

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dependence(
        @SerialName("id") val id: Int,
        @SerialName("dependencia") var dependence: String? = null,
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
        parcel.writeString(dependence)
        idResidence?.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dependence> {
        override fun createFromParcel(parcel: Parcel): Dependence {
            return Dependence(parcel)
        }

        override fun newArray(size: Int): Array<Dependence?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class DependenceResponse(
    @SerialName("data") val data: List<Dependence>? = null,
)