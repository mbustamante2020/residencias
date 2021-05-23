package com.residencias.es.data.network

object Endpoints {

    // URLs base
    private const val urlBase     = "https://residenciasysalud.es"
    private const val urlBaseApi  = "$urlBase/api"
    private const val urlBaseAuth = "$urlBaseApi/auth"

    // URL base de imágenes que se mostrarán en pantalla
    const val urlImagen = "$urlBase/img/residencia"

    // Autenticación
    const val urlAuthRegister    = "$urlBaseAuth/register"
    const val urlAuthLogin       = "$urlBaseAuth/login"
    const val urlAuthLoginGoogle = "$urlBaseAuth/login/google"
    const val urlAuthRefresh     = "$urlBaseAuth/refresh"

    //Perfil del usuario y residencia
    const val urlUser       = "$urlBaseApi/user"
    const val urlResidence  = "$urlBaseApi/residence"

    //Campos perfil de la residencia
    const val urlResidenceRooms       = "$urlBaseApi/rooms"
    const val urlResidenceSectors     = "$urlBaseApi/sectors"
    const val urlResidenceDependences = "$urlBaseApi/dependences"

    //obtener listado de residencias
    const val urlResidences = "$urlBaseApi/residences"
    const val urlResidencesMap = "$urlBaseApi/residences/map"

    //Campos del buscador
    const val urlProvinces      = "$urlBaseApi/residences/provinces"
    const val urlTowns          = "$urlBaseApi/residences/towns"
    const val urlRooms          = "$urlBaseApi/residences/rooms"
    const val urlSectors        = "$urlBaseApi/residences/sectors"
    const val urlDependences    = "$urlBaseApi/residences/dependences"
    const val urlPrices         = "$urlBaseApi/residences/prices"

    //Imagenes
    const val urlGetImages   = "$urlBaseApi/images"
    const val urlUpdateImage = "$urlBaseApi/image"
    const val urlUploadImage = "$urlBaseApi/image/upload"
    const val urlDeleteImage = "$urlBaseApi/image/delete"
}