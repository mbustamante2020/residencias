package com.residencias.es.data.residence

import android.os.Parcel
import android.os.Parcelable

data class Search(
    var search_for: String? = null,
    var province: Int? = null,
    var town: Int? = null,
    var price: Int? = null,
    var room: Int? = null,
    var sector: Int? = null,
    var dependence: Int? = null,
    var is_map: Int = 0,
    var near: Boolean = false,
    var latitude: Double? = null,
    var longitude: Double? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(search_for)
        parcel.writeValue(province)
        parcel.writeValue(town)
        parcel.writeValue(price)
        parcel.writeValue(room)
        parcel.writeValue(sector)
        parcel.writeValue(dependence)
        parcel.writeInt(is_map)
        parcel.writeByte(if (near) 1 else 0)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Search> {
        override fun createFromParcel(parcel: Parcel): Search {
            return Search(parcel)
        }

        override fun newArray(size: Int): Array<Search?> {
            return arrayOfNulls(size)
        }
    }
}