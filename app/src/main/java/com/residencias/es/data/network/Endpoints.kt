package com.residencias.es.data.network

object Endpoints {

    private const val apiBaseUrl = "https://residenciasysalud.es"

    // OAuth2 API Endpoints api/register
    private const val oauthBaseUrl = "$apiBaseUrl/api"


    //const val authorizationUrl = "$oauthBaseUrl/authorize"
    //const val tokenUrl = "$oauthBaseUrl/token"



    const val login = "$oauthBaseUrl/login"
    const val register = "$oauthBaseUrl/register"
    const val user = "$oauthBaseUrl/user"
    const val updateUser = "$oauthBaseUrl/profile/user"
    const val residence = "$oauthBaseUrl/residence"
    const val updateResidence = "$oauthBaseUrl/profile/residence"


    const val residenceRooms = "$oauthBaseUrl/rooms"
    const val residenceSectors = "$oauthBaseUrl/sectors"
    const val residenceDependences = "$oauthBaseUrl/dependences"
    const val residenceUploadImage = "$oauthBaseUrl/upload"


    const val messages = "$oauthBaseUrl/message"                    //hilos de conversaciones
    const val messagesSend = "$oauthBaseUrl/message/add"            //envia mensaje
    const val messagesMyMessages = "$oauthBaseUrl/message/messages" //listado de conversaciones


    // API Endpoints
    const val imagenUrl = "$apiBaseUrl/img/residencia"
    const val residencesUrl = "$apiBaseUrl/api/v1/residencias"


    //Para el buscador
    const val provinciasUrl = "$apiBaseUrl/api/v1/provincias" // api/v1/provincias
    const val townsUrl = "$apiBaseUrl/api/v1/municipios" // api/v1/municipios/{provincia}

    const val pricesUrl         = "$apiBaseUrl/api/v1/precios"          // api/v1/precios
    const val roomsUrl          = "$apiBaseUrl/api/v1/habitaciones"     // api/v1/habitaciones
    const val sectorsUrl        = "$apiBaseUrl/api/v1/plazas"           // api/v1/plazas
    const val dependencesUrl    = "$apiBaseUrl/api/v1/dependencias"     // api/v1/dependencias
    const val serviciosUrl      = "$apiBaseUrl/api/v1/servicios"        // api/v1/servicios

}