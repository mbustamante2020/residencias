package com.residencias.es.data.residences

import java.io.File


interface ResidencesRepository {
    // Pair<first, second>
    // first: pagina actual
    // second: listado de Residences
    suspend fun getResidences(pagina: Int?, search: Search?): Pair<Int?, List<Residence>?>?

    suspend fun getProvinces(todas: Boolean?): List<Province>?

    suspend fun getTowns(idprovincia: Int, todas: Boolean?): List<Town>?

    suspend fun getRooms(): List<Room>?

    suspend fun getSectors(): List<Sector>?

    suspend fun getDependences(): List<Dependence>?

    suspend fun getPrices(): List<Price>?

    suspend fun onUnauthorized()

    //////////////// MI RESIDENCIA ////////////////////////
    suspend fun getMyResidence(accessToken: String?): Residence?

    suspend fun updateMyResidence(accessToken: String?, residence: Residence?, dependence: String, sector: String, room: String): Residence?

    suspend fun getMyResidenceRooms(accessToken: String?): List<Room>?

    suspend fun getMyResidenceSectors(accessToken: String?): List<Sector>?

    suspend fun getMyResidenceDependences(accessToken: String?): List<Dependence>?

    suspend fun uploadFile(accessToken: String?, file: File, headerValue: String)//: HttpResponse?
}