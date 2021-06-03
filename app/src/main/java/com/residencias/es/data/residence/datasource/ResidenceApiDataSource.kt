package com.residencias.es.data.residence.datasource

import android.util.Log
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.residence.model.*
import io.ktor.client.*
import io.ktor.client.request.*

class ResidenceApiDataSource(private val httpClient: HttpClient) {

    private val tag: String = "ResidenceRemoteDataSource"

    /*********** MI RESIDENCIA ************/
    // se obtienen los datos del usuario
    suspend fun getMyResidence(): Residence? {
        return try {
            val response = httpClient.get<ResidenceResponse>(Endpoints.urlResidence)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    suspend fun updateMyResidence(user: Residence?, dependence: String, sector: String, room: String): Residence? {
        try {
            val response = httpClient.put<ResidenceResponse>(Endpoints.urlResidence) {
                    parameter("dependence", dependence)
                    parameter("sector", sector)
                    parameter("room", room)

                    user?.let {
                        parameter("name", it.name)
                        parameter("email", it.email)
                        parameter("web", it.web)
                        parameter("address", it.address)
                        parameter("phone", it.phone)
                        parameter("price", it.price)
                        parameter("description", it.description)
                        parameter("latitude", it.latitude)
                        parameter("longitude", it.longitude)
                        parameter("idtown", it.idtown)
                    }
                }
            return response.data//?.firstOrNull()
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            return null
        }
    }

    // se obtienen todas las habitaciones
    suspend fun getMyResidenceRooms(): List<Room>? {
        return try {
            val response = httpClient.get<RoomResponse>(Endpoints.urlResidenceRooms)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todas los tipos de plazas
    suspend fun getMyResidenceSectors(): List<Sector>? {
        return try {
            val response = httpClient.get<SectorResponse>(Endpoints.urlResidenceSectors)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            return null
        }
    }

    // se obtienen todos los tipos de dependencias
    suspend fun getMyResidenceDependencies(): List<Dependence>? {
        return try {
            val response = httpClient.get<DependenceResponse>(Endpoints.urlResidenceDependencies)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            return null
        }
    }


    /*********** RESIDENCIAS ************/
    // Se obtienen las residencias
    suspend fun getResidences(page: Int? = null, search: Search? = null): Pair<Int?, List<Residence>?>? {
        return try {
            val response = httpClient.get<ResidencesResponse>(Endpoints.urlResidences) {
                page?.let {
                    parameter("page", page)
                }
                search?.let { s ->
                    s.search_for?.let { parameter("buscar_por", it) }
                    s.province?.let {   parameter("provincia",  it) }
                    s.town?.let {       parameter("municipio", it)  }
                    s.price?.let {      parameter("precio", it)     }
                    s.room?.let {       parameter("habitacion", it) }
                    s.sector?.let {     parameter("plaza", it)      }
                    s.dependence?.let { parameter("dependencia", it)}
                }
            }
            val nextPage = response.current_page?.plus(1)
            Pair(nextPage, response.data)
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    // Se obtienen las residencias a ser mostradas en el mapa
    suspend fun getResidencesMap(page: Int? = null, search: Search? = null): Pair<Int?, List<Residence>?>? {
        Log.i("nearSwitch", "dataSource = ${search?.near} ")
        return try {
            val response = httpClient.get<ResidencesResponse>(Endpoints.urlResidencesMap) {
                page?.let {
                    parameter("page", page)
                }
                search?.let { s ->
                    s.search_for?.let { parameter("buscar_por", it) }
                    s.province?.let {   parameter("provincia",  it) }
                    s.town?.let {       parameter("municipio", it)  }
                    s.price?.let {      parameter("precio", it)     }
                    s.room?.let {       parameter("habitacion", it) }
                    s.sector?.let {     parameter("plaza", it)      }
                    s.dependence?.let { parameter("dependencia", it)}
                    //s.near?.let {     parameter("mapa", it)       }
                    s.latitude?.let {   parameter("latitud", it)    }
                    s.longitude?.let {  parameter("longitud", it)   }

                    parameter("mapa", true)
                }
            }
            val nextPage = response.current_page?.plus(1)
            Pair(nextPage, response.data)
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }


    /*********** BUSCAR ************/
    // se obtienen todas las provincias
    suspend fun getProvinces(all: Boolean?): List<Province>? {
        return try {
            val response = httpClient.get<ProvinceResponse>(Endpoints.urlProvinces) {
                all?.let { parameter("todas", all)  }
            }
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todos los municipios de una provincia
    suspend fun getTowns(idprovince: Int? = null, all: Boolean?): List<Town>? {
        return try {
            val response = httpClient.get<TownResponse>("${Endpoints.urlTowns}/$idprovince") {
                all?.let { parameter("todas", all) }
            }
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todas las habitaciones
    suspend fun getRooms(): List<Room>? {
        return try {
            val response = httpClient.get<RoomResponse>(Endpoints.urlRooms)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todas los tipos de plazas
    suspend fun getSectors(): List<Sector>? {
        return try {
            val response = httpClient.get<SectorResponse>(Endpoints.urlSectors)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todos los tipos de dependencias
    suspend fun getDependencies(): List<Dependence>? {
        return try {
            val response = httpClient.get<DependenceResponse>(Endpoints.urlDependencies)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todos los tipos de dependencias
    suspend fun getPrices(): List<Price>? {
        return try {
            val response = httpClient.get<PriceResponse>(Endpoints.urlPrices)
            response.data
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

}