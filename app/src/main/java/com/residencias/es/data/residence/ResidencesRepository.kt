package com.residencias.es.data.residence

import com.residencias.es.data.residence.model.*


interface ResidencesRepository {
    // Pair<first, second>
    // first: pagina actual
    // second: listado de Residences
    suspend fun getResidences(page: Int?, search: Search?): Pair<Int?, List<Residence>?>?

    suspend fun getResidencesMap(page: Int?, search: Search?): Pair<Int?, List<Residence>?>?

    suspend fun getProvinces(all: Boolean?): List<Province>?

    suspend fun getTowns(idProvince: Int, all: Boolean?): List<Town>?

    suspend fun getRooms(): List<Room>?

    suspend fun getSectors(): List<Sector>?

    suspend fun getDependencies(): List<Dependence>?

    suspend fun getPrices(): List<Price>?

    suspend fun onUnauthorized()

    //////////////// MI RESIDENCIA ////////////////////////
    suspend fun getMyResidence(accessToken: String?): Residence?

    suspend fun updateMyResidence(accessToken: String?, residence: Residence?, dependence: String, sector: String, room: String): Residence?

    suspend fun getMyResidenceRooms(accessToken: String?): List<Room>?

    suspend fun getMyResidenceSectors(accessToken: String?): List<Sector>?

    suspend fun getMyResidenceDependencies(accessToken: String?): List<Dependence>?

}