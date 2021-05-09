package com.residencias.es.data.residences

import android.os.Parcel
import android.os.Parcelable

data class Search(
        var search_for: String? = null,
        var province: Int? = null,
        var town: Int? = null,
        var price: Int? = null,
        var size_residence: Int? = null,
        var room: Int? = null,
        var sector: Int? = null,
        var dependence: Int? = null,
        var services: Int? = null,
        var family_respite: Int? = null,
        var sectors_available: Int? = null,
        var under_65: Int? = null

        ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(search_for)
        province?.let { parcel.writeInt(it) }
        town?.let { parcel.writeInt(it) }
        price?.let { parcel.writeInt(it) }
        size_residence?.let { parcel.writeInt(it) }
        room?.let { parcel.writeInt(it) }
        sector?.let { parcel.writeInt(it) }
        dependence?.let { parcel.writeInt(it) }
        services?.let { parcel.writeInt(it) }
        family_respite?.let { parcel.writeInt(it) }
        sectors_available?.let { parcel.writeInt(it) }
        under_65?.let { parcel.writeInt(it) }
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