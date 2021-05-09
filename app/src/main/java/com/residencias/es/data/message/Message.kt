package com.residencias.es.data.message

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
        @SerialName("id") val id: Int? = null,
        @SerialName("message") var message: String? = null,
        @SerialName("date") var date: String? = null,
        @SerialName("iduseremitter") var idUserEmitter : Int? = null,
        @SerialName("iduserreceiver") var idUserReceiver: Int? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("email") var email: String? = null,
        @SerialName("phone") var phone: String? = null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(message)
        parcel.writeString(date)
        parcel.writeValue(idUserEmitter)
        parcel.writeValue(idUserReceiver)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class MessageResponse(
        @SerialName("data") val data: List<Message>? = null,
)