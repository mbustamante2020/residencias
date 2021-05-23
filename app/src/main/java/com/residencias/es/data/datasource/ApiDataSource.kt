package com.residencias.es.data.datasource

import android.util.Log
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.oauth.Constants
import com.residencias.es.data.oauth.OAuthTokensResponse
import com.residencias.es.data.residence.*
import com.residencias.es.data.user.User
import com.residencias.es.data.user.UserResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

class ApiDataSource( private val httpClient: HttpClient )  {

    private val TAG: String = "ApiDataSource"

    /*********** AUTENTICACIÓN ************/
    suspend fun login(email: String, password: String): OAuthTokensResponse? {
        return try {
            httpClient
                .post<OAuthTokensResponse>(Endpoints.urlAuthLogin) {
                    parameter("client_secret", Constants.clientSecret)
                    parameter("email", email)
                    parameter("password", password)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    suspend fun loginGoogle(email: String, name: String): OAuthTokensResponse? {
        return try {
            httpClient
                .post<OAuthTokensResponse>(Endpoints.urlAuthLoginGoogle) {
                    parameter("client_secret", Constants.clientSecret)
                    parameter("email", email)
                    parameter("name", name)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    suspend fun register(name: String, email: String, password: String): OAuthTokensResponse? {
        return try {
            httpClient
                .post<OAuthTokensResponse>(Endpoints.urlAuthRegister) {
                    parameter("client_secret", Constants.clientSecret)
                    parameter("name", name)
                    parameter("email", email)
                    parameter("password", password)
                    parameter("password_confirmation", password)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    suspend fun refresh(accessToken: String?): OAuthTokensResponse? {
        return try {
            httpClient
                .post<OAuthTokensResponse>(Endpoints.urlAuthRefresh) {
                    parameter("client_secret", Constants.clientSecret)
                    parameter("token", accessToken)
                }
        } catch (t: Throwable) {
            //Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }


    /*********** PERFIL ************/
    // se obtienen los datos del usuario
    suspend fun getUser(accessToken: String?): User? {
        return try {
            val response = httpClient.get<UserResponse>(Endpoints.urlUser){
                accessToken?.let {
                    parameter("token", it)
                }
            }
            //response.data?.firstOrNull()
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    suspend fun updateUser(accessToken: String?, user: User?): User? {
        try {
            val response = httpClient
                .put<UserResponse>(Endpoints.urlUser) {
                    parameter("token", accessToken)
                    user?.let {
                        parameter("username", it.userName)
                        parameter("name", it.name)
                        parameter("address", it.address)
                        parameter("phone", it.phone)
                    }
                }
            return response.data//?.firstOrNull()
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            return null
        }
    }

    /*********** MI RESIDENCIA ************/
    // se obtienen los datos del usuario
    suspend fun getMyResidence(accessToken: String?): Residence? {
        return try {
            val response = httpClient.get<ResidenceResponse>(Endpoints.urlResidence){
                accessToken?.let {
                    parameter("token", it)
                }
            }
            //response.data?.firstOrNull()
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    suspend fun updateMyResidence(accessToken: String?, user: Residence?, dependence: String, sector: String, room: String): Residence? {
        try {
            val response = httpClient
                .put<ResidenceResponse>(Endpoints.urlResidence) {
                    parameter("token", accessToken)

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
            Log.w(TAG, "Error Getting Access token", t)
            return null
        }
    }


    // se obtienen todas las habitaciones
    suspend fun getMyResidenceRooms(accessToken: String?): List<Room>? {
        return try {
            val response = httpClient.get<RoomResponse>("${Endpoints.urlResidenceRooms}") {
                parameter("token", accessToken)
            }
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todas los tipos de plazas
    suspend fun getMyResidenceSectors(accessToken: String?): List<Sector>? {
        return try {
            val response = httpClient.get<SectorResponse>("${Endpoints.urlResidenceSectors}") {
                parameter("token", accessToken)
            }
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            return null
        }
    }

    // se obtienen todos los tipos de dependencias
    suspend fun getMyResidenceDependences(accessToken: String?): List<Dependence>? {
        return try {
            val response = httpClient.get<DependenceResponse>("${Endpoints.urlResidenceDependences}") {
                parameter("token", accessToken)
            }
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            return null
        }
    }












    /*********** RESIDENCIA ************/
    suspend fun getResidence(accessToken: String?): Residence? {
        return try {
            val response = httpClient.get<ResidenceResponse>(Endpoints.urlResidence){
                accessToken?.let {
                    parameter("token", it)
                }
            }
            //response.data?.firstOrNull()
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }


    /*********** RESIDENCIAS ************/
    // Se obtienen las residencias
    suspend fun getResidences(page: Int? = null, search: Search? = null): Pair<Int?, List<Residence>?>? {
        Log.i("ApiDataSource", "getResidences 364 page $page")
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
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    // Se obtienen las residencias a ser mostradas en el mapa
    suspend fun getResidencesMap(page: Int? = null, search: Search? = null): Pair<Int?, List<Residence>?>? {
        Log.i("search-->", "api $search")
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
            Log.w(TAG, "Error Getting Access token", t)
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
            Log.w(TAG, "Error Getting Access token", t)
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
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todas las habitaciones
    suspend fun getRooms(): List<Room>? {
        return try {
            val response = httpClient.get<RoomResponse>("${Endpoints.urlRooms}")
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todas los tipos de plazas
    suspend fun getSectors(): List<Sector>? {
        return try {
            val response = httpClient.get<SectorResponse>("${Endpoints.urlSectors}")
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todos los tipos de dependencias
    suspend fun getDependences(): List<Dependence>? {
        return try {
            val response = httpClient.get<DependenceResponse>("${Endpoints.urlDependences}")
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    // se obtienen todos los tipos de dependencias
    suspend fun getPrices(): List<Price>? {
        return try {
            val response = httpClient.get<PriceResponse>("${Endpoints.urlPrices}")
            response.data
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }
}