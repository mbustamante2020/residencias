package com.residencias.es.data.residence

import com.residencias.es.data.datasource.ApiDataSource
import com.residencias.es.data.datasource.SessionManager


class ResidencesRepositoryImpl(
        private val apiDataSource: ApiDataSource,
        private val localDataSource: SessionManager
) : ResidencesRepository {

    override suspend fun getResidences(page: Int?, search: Search?): Pair<Int?, List<Residence>?>? {
        return apiDataSource.getResidences(page, search)
    }

    override suspend fun getResidencesMap(page: Int?, search: Search?): Pair<Int?, List<Residence>?>? {
        return apiDataSource.getResidencesMap(page, search)
    }

    override suspend fun getProvinces(all: Boolean?): List<Province>? {
        return apiDataSource.getProvinces(all)
    }

    override suspend fun getTowns(idProvince: Int, all: Boolean?): List<Town>? {
        return apiDataSource.getTowns(idProvince, all)
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
        localDataSource.clearAccessToken()
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
}