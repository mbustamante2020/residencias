package com.residencias.es.data.residences

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Province(
        @SerialName("id") val id: Int,
        @SerialName("provincia") var province: String? = null,

        ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(province)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Province> {
        override fun createFromParcel(parcel: Parcel): Province {
            return Province(parcel)
        }

        override fun newArray(size: Int): Array<Province?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class ProvinceResponse(
    @SerialName("data") val data: List<Province>? = null,
)