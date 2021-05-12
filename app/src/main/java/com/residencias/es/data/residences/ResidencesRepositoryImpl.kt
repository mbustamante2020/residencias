package com.residencias.es.data.residences

import com.residencias.es.data.datasource.ApiDataSource
import com.residencias.es.data.datasource.SessionManager
import java.io.File


class ResidencesRepositoryImpl(
        private val apiDataSource: ApiDataSource,
        private val sharedPreferencesDataSource: SessionManager
) : ResidencesRepository {

    override suspend fun getResidences(pagina: Int?, search: Search?): Pair<Int?, List<Residence>?>? {
        return apiDataSource.getResidences(pagina, search)
    }

    override suspend fun getProvinces(todas: Boolean?): List<Province>? {
        return apiDataSource.getProvinces(todas)
    }

    override suspend fun getTowns(idprovincia: Int, todas: Boolean?): List<Town>? {
        return apiDataSource.getTowns(idprovincia, todas)
    }

    override suspend fun getRooms(): List<Room>? {
        return apiDataSource.getRooms()
    }

    override suspend fun getSectors(): List<Sector>? {
        return apiDataSource.getSectors()
    }

    override suspend fun getDependences(): List<Dependence>? {
        return apiDataSource.getDependences()
    }

    override suspend fun getPrices(): List<Price>? {
        return apiDataSource.getPrices()
    }

    override suspend fun onUnauthorized() {
        sharedPreferencesDataSource.clearAccessToken()
    }

    //////////////// MI RESIDENCIA ////////////////////////
    override suspend fun getMyResidence(accessToken: String?): Residence? {
        return apiDataSource.getMyResidence(accessToken)
    }

    override suspend fun updateMyResidence(accessToken: String?, residence: Residence?, dependence: String, sector: String, room: String): Residence? {
        return apiDataSource.updateMyResidence(accessToken, residence, dependence, sector, room)
    }

    override suspend fun getMyResidenceRooms(accessToken: String?): List<Room>? {
        return apiDataSource.getMyResidenceRooms(accessToken)
    }

    override suspend fun getMyResidenceSectors(accessToken: String?): List<Sector>? {
        return apiDataSource.getMyResidenceSectors(accessToken)
    }

    override suspend fun getMyResidenceDependences(accessToken: String?): List<Dependence>? {
        return apiDataSource.getMyResidenceDependences(accessToken)
    }

    override suspend fun uploadFile(accessToken: String?, file: File, headerValue: String) { //: HttpResponse? {
        apiDataSource.uploadFile(accessToken, file, headerValue)
    }
}