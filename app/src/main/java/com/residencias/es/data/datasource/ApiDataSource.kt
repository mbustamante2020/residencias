package com.residencias.es.data.datasource

import android.util.Log
import com.residencias.es.data.ApiService
import com.residencias.es.data.message.Message
import com.residencias.es.data.oauth.TokenResponse
import com.residencias.es.data.residences.*
import com.residencias.es.data.user.User
import io.ktor.client.*

class ApiDataSource( httpClient: HttpClient )  {

    private val service = ApiService(httpClient)

    /*********** ACCESO ************/
    suspend fun login(email: String, password: String): TokenResponse? {
        return service.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): TokenResponse? {
        return service.register(name, email, password)
    }

    /*********** PERFIL ************/
    suspend fun getUser(accessToken: String?): User? {
        return service.getUser(accessToken)
    }

    suspend fun updateUser(accessToken: String?, user: User?): User? {
        return service.updateUser(accessToken, user)
    }

    /*********** MI RESIDENCIA ************/
    suspend fun getMyResidence(accessToken: String?): Residence? {
        return service.getMyResidence(accessToken)
    }

    suspend fun updateMyResidence(accessToken: String?, residence: Residence?, dependence: String, sector: String, room: String): Residence? {
        return service.updateMyResidence(accessToken, residence, dependence, sector, room)
    }

    suspend fun getMyResidenceRooms(accessToken: String?): List<Room>? {
        return service.getMyResidenceRooms(accessToken)?.data
    }

    suspend fun getMyResidenceSectors(accessToken: String?): List<Sector>? {
        return service.getMyResidenceSectors(accessToken)?.data
    }

    suspend fun getMyResidenceDependences(accessToken: String?): List<Dependence>? {
        return service.getMyResidenceDependences(accessToken)?.data
    }

    suspend fun uploadFile(accessToken: String?, file: ByteArray, headerValue: String) { //: HttpResponse?  {
        service.uploadFile(accessToken, file, headerValue)
    }





    /*********** RESIDENCIA ************/
    suspend fun getResidence(accessToken: String?): Residence? {
        return service.getResidence(accessToken)
    }

    suspend fun updateResidence(accessToken: String?, residence: Residence?): Residence? {
        return service.updateResidence(accessToken, residence)
    }

    /*********** RESIDENCIAS ************/
    // se obtienen el listado de residences
    suspend fun getResidences(pagina: Int?, search: Search?): Pair<Int?, List<Residence>?> {
        Log.i("current_page", "ApiDataSource 31 $pagina")
        val residences: ResidencesResponse? = service.getResidences(pagina, search)
        return Pair(residences?.current_page, residences?.data)
    }

    /*********** BUSCAR ************/
    suspend fun getProvinces(todas: Boolean?): List<Province>? {
        return service.getProvinces(todas)?.data
    }

    suspend fun getTowns(idprovincia: Int?, todas: Boolean?): List<Town>? {
        return service.getTowns(idprovincia, todas)?.data
    }

    suspend fun getRooms(): List<Room>? {
        return service.getRooms()?.data
    }

    suspend fun getSectors(): List<Sector>? {
        return service.getSectors()?.data
    }

    suspend fun getDependences(): List<Dependence>? {
        return service.getDependences()?.data
    }

    /****************** MENSAJES ********************/
    suspend fun sendMessage(accessToken: String?, message: Message): List<Message>? {
        return service.sendMessage(accessToken, message)
    }

    suspend fun getMessages(accessToken: String?, message: Message): List<Message>? {
        return service.getMessages(accessToken, message)
    }

    suspend fun getMyMessages(accessToken: String?, message: Message): List<Message>? {
        return service.getMyMessages(accessToken, message)
    }
}