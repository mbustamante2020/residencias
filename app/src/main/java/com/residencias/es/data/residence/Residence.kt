package com.residencias.es.data.residence

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Residence(
    @SerialName("id") val id: String,
    @SerialName("nombre") var name: String? = null,
    @SerialName("email") var email: String? = null,
    @SerialName("web") var web: String? = null,
    @SerialName("precio") val price: String? = null,
    @SerialName("plazas_libres") val sectors: String? = null,
    @SerialName("telefono") val phone: String? = null,
    @SerialName("direccion") val address: String? = null,
    @SerialName("provincia") val province: String? = null,
    @SerialName("municipio") val town: String? = null,
    @SerialName("imagen_url") val urlImagen: String? = null,
    @SerialName("descripcion_resumen") val description: String? = null,
    @SerialName("latitud") val latitude: String? = null,
    @SerialName("longitud") val longitude: String? = null,
    @SerialName("provincia_id") val idprovince: String? = null,
    @SerialName("municipios_id") val idtown: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(web)
        parcel.writeString(price)
        parcel.writeString(sectors)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(province)
        parcel.writeString(town)
        parcel.writeString(urlImagen)
        parcel.writeString(description)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(idprovince)
        parcel.writeString(idtown)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Residence> {
        override fun createFromParcel(parcel: Parcel): Residence {
            return Residence(parcel)
        }

        override fun newArray(size: Int): Array<Residence?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class ResidenceResponse(
    @SerialName("residencia") val data: Residence? = null,
)

@Serializable
data class ResidencesResponse(
    @SerialName("data") val data: List<Residence>? = null,
    @SerialName("pagination") val pagination: Pagination? = null,
    @SerialName("current_page") val current_page: Int? = null,
)

@Serializable
data class Pagination(
    @SerialName("cursor") val cursor: String? = null,
)