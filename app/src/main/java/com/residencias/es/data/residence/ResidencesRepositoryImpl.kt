package com.residencias.es.data.residence

import com.residencias.es.data.oauth.datasource.SessionManager
import com.residencias.es.data.residence.datasource.ResidenceApiDataSource
import com.residencias.es.data.residence.model.*


class ResidencesRepositoryImpl(
    private val apiDataSource: ResidenceApiDataSource,
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

    override suspend fun getDependencies(): List<Dependence>? {
        return apiDataSource.getDependencies()
    }

    override suspend fun getPrices(): List<Price>? {
        return apiDataSource.getPrices()
    }

    override suspend fun onUnauthorized() {
        localDataSource.clearAccessToken()
    }

    //////////////// MI RESIDENCIA ////////////////////////
    override suspend fun getMyResidence(): Residence? {
        return apiDataSource.getMyResidence()
    }

    override suspend fun updateMyResidence(residence: Residence?, dependence: String, sector: String, room: String): Residence? {
        return apiDataSource.updateMyResidence(residence, dependence, sector, room)
    }

    override suspend fun getMyResidenceRooms(): List<Room>? {
        return apiDataSource.getMyResidenceRooms()
    }

    override suspend fun getMyResidenceSectors(): List<Sector>? {
        return apiDataSource.getMyResidenceSectors()
    }

    override suspend fun getMyResidenceDependencies(): List<Dependence>? {
        return apiDataSource.getMyResidenceDependencies()
    }
}