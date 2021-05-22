package com.residencias.es.data.network

object Endpoints {

    private const val urlApiBase = "https://residenciasysalud.es"
    private const val authBaseUrl = "$urlApiBase/api/auth"
    private const val oauthBaseUrl = "$urlApiBase/api"

    // Autenticación
    const val urlAuthRegister = "$authBaseUrl/register"
    const val urlAuthLogin = "$authBaseUrl/login"
    const val urlAuthLoginGoogle = "$authBaseUrl/login/google"
    const val urlAuthRefresh = "$authBaseUrl/refresh"









    const val urlUser = "$oauthBaseUrl/user"
   // const val urlUpdateUser = "$oauthBaseUrl/profile/user"
    const val urlResidence = "$oauthBaseUrl/residence"
    //const val urlUpdateResidence = "$oauthBaseUrl/profile/residence"


    const val urlResidenceRooms = "$oauthBaseUrl/rooms"
    const val urlResidenceSectors = "$oauthBaseUrl/sectors"
    const val urlResidenceDependences = "$oauthBaseUrl/dependences"



    const val urlMessages = "$oauthBaseUrl/message"                    //hilos de conversaciones
    const val urlMessagesSend = "$oauthBaseUrl/messages/send"            //envia mensaje
    const val urlMyMessages = "$oauthBaseUrl/messages" //listado de conversaciones



    // API imagen
    const val urlImagen = "$urlApiBase/img/residencia"

    //obtener listado de residencias
    const val urlResidences = "$urlApiBase/api/v1/residences"


    //Para el buscador
    const val urlProvinces      = "$urlApiBase/api/v1/provinces"
    const val urlTowns          = "$urlApiBase/api/v1/towns"
    const val urlRooms          = "$urlApiBase/api/v1/rooms"
    const val urlSectors        = "$urlApiBase/api/v1/sectors"
    const val urlDependences    = "$urlApiBase/api/v1/dependences"
    const val urlPrices    = "$urlApiBase/api/v1/prices"

    //Imagenes
    const val urlGetImages   = "$oauthBaseUrl/images"
    const val urlUpdateImage = "$oauthBaseUrl/image"
    const val urlUploadImage = "$oauthBaseUrl/image/upload"
    const val urlDeleteImage = "$oauthBaseUrl/image/delete"
}