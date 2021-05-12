package com.residencias.es.data.datasource

import android.util.Log
import android.webkit.MimeTypeMap
import com.residencias.es.data.message.Message
import com.residencias.es.data.message.MessageResponse
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.oauth.Constants
import com.residencias.es.data.oauth.OAuthTokensResponse
import com.residencias.es.data.residences.*
import com.residencias.es.data.user.User
import com.residencias.es.data.user.UserResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ApiDataSource( private val httpClient: HttpClient )  {

    private val TAG: String = "ApiDataSource"

    /*********** ACCESO ************/
    suspend fun login(email: String, password: String): OAuthTokensResponse? {
        return try {
            httpClient.post<OAuthTokensResponse>(Endpoints.urlLogin) {
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
            httpClient.post<OAuthTokensResponse>(Endpoints.urlLoginGoogle) {
                parameter("clientsecret", Constants.clientSecret)
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
            httpClient.post<OAuthTokensResponse>(Endpoints.urlRegister) {
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

/*
    suspend fun getTokens(authorizationCode: String): TokenResponse? {
        return try {
            httpClient.post<TokenResponse>(Endpoints.tokenUrl) {
                        parameter("client_id", Constants.clientID)
                        parameter("client_secret", Constants.clientSecret)
                        parameter("code", authorizationCode)
                        parameter("grant_type", "authorization_code")
                        parameter("redirect_uri", Constants.redirectUri)
                    }
        } catch (t: Throwable) {
            //Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }*/

    /*********** PERFIL ************/
    // se obtienen los datos del usuario
    @Throws(UnauthorizedException::class)
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
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    @Throws(UnauthorizedException::class)
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
            return when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    /*********** MI RESIDENCIA ************/
    // se obtienen los datos del usuario
    @Throws(UnauthorizedException::class)
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
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    @Throws(UnauthorizedException::class)
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
            return when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }






    // se obtienen todas las habitaciones
    @Throws(UnauthorizedException::class)
    suspend fun getMyResidenceRooms(accessToken: String?): List<Room>? {
        return try {
            val response = httpClient.get<RoomResponse>("${Endpoints.urlResidenceRooms}") {
                parameter("token", accessToken)
            }
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    // se obtienen todas los tipos de plazas
    @Throws(UnauthorizedException::class)
    suspend fun getMyResidenceSectors(accessToken: String?): List<Sector>? {
        return try {
            val response = httpClient.get<SectorResponse>("${Endpoints.urlResidenceSectors}") {
                parameter("token", accessToken)
            }
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    // se obtienen todos los tipos de dependencias
    @Throws(UnauthorizedException::class)
    suspend fun getMyResidenceDependences(accessToken: String?): List<Dependence>? {
        return try {
            val response = httpClient.get<DependenceResponse>("${Endpoints.urlResidenceDependences}") {
                parameter("token", accessToken)
            }
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    @Throws(UnauthorizedException::class)
    fun uploadFile(accessToken: String?, sourceFile: File, uploadedFileName: String? = null) {
        Thread {
            val mimeType = getMimeType(sourceFile);
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String = uploadedFileName ?: sourceFile.name

            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image", fileName, sourceFile.asRequestBody(mimeType.toMediaTypeOrNull()))
                            .addFormDataPart("token", accessToken.toString())
                        .build()

                val request: Request = Request.Builder().url(Endpoints.urlResidenceUploadImage).post(requestBody).build()

                val response: Response = OkHttpClient().newCall(request).execute()


                Log.e("File upload", "name $uploadedFileName}{ ${sourceFile.name}")
                Log.e("File upload", "name $fileName")
                if (response.isSuccessful) {
                    Log.d("File upload","success, path: $fileName $response")
                    //showToast("File uploaded successfully at $serverUploadDirectoryPath$fileName")
                } else {
                    Log.e("File upload", "failed $response")
                    //showToast("File uploading failed")
                }
                response.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("File upload", "failed")
                //showToast("File uploading failed")
            }


        }.start()
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }










    /*********** RESIDENCIA ************/
    @Throws(UnauthorizedException::class)
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
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }


    /*********** RESIDENCIAS ************/
    // Se obtienen las Residences
    @Throws(UnauthorizedException::class)
    suspend fun getResidences(page: Int? = null, search: Search? = null): Pair<Int?, List<Residence>?>? {
        Log.i("current_page", "apiservice 42 $page")
        return try {
            val response = httpClient.get<ResidencesResponse>(Endpoints.urlResidences) {
                page?.let {
                    parameter("page", page)
                }
                search?.let { it ->
                    it.search_for?.let { parameter("buscar_por", it) }
                    it.province?.let {   parameter("provincia",  it) }
                    it.town?.let {       parameter("municipio", it)  }
                    it.price?.let {      parameter("precio", it)     }
                    it.room?.let {       parameter("habitacion", it) }
                    it.sector?.let {     parameter("plaza", it)      }
                    it.dependence?.let { parameter("dependencia", it)}
                }
            }
            val nextPage = response.current_page?.plus(1)
            Pair(nextPage, response.data)
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    /*********** BUSCAR ************/
    // se obtienen todas las provincias
    @Throws(UnauthorizedException::class)
    suspend fun getProvinces(all: Boolean?): List<Province>? {
        return try {
            val response = httpClient.get<ProvinceResponse>(Endpoints.urlProvinces) {
                all?.let { parameter("todas", all)  }
            }
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    // se obtienen todos los municipios de una provincia
    @Throws(UnauthorizedException::class)
    suspend fun getTowns(idprovince: Int? = null, all: Boolean?): List<Town>? {
        return try {
            val response = httpClient.get<TownResponse>("${Endpoints.urlTowns}/$idprovince") {
                all?.let { parameter("todas", all) }
            }
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    // se obtienen todas las habitaciones
    @Throws(UnauthorizedException::class)
    suspend fun getRooms(): List<Room>? {
        return try {
            val response = httpClient.get<RoomResponse>("${Endpoints.urlRooms}")
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    // se obtienen todas los tipos de plazas
    @Throws(UnauthorizedException::class)
    suspend fun getSectors(): List<Sector>? {
        return try {
            val response = httpClient.get<SectorResponse>("${Endpoints.urlSectors}")
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    // se obtienen todos los tipos de dependencias
    @Throws(UnauthorizedException::class)
    suspend fun getDependences(): List<Dependence>? {
        return try {
            val response = httpClient.get<DependenceResponse>("${Endpoints.urlDependences}")
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    // se obtienen todos los tipos de dependencias
    @Throws(UnauthorizedException::class)
    suspend fun getPrices(): List<Price>? {
        return try {
            val response = httpClient.get<PriceResponse>("${Endpoints.urlPrices}")
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }




    /****************** MENSAJES ********************/
    suspend fun sendMessage(accessToken: String?, message: Message): List<Message>? {
        return try {
            val response = httpClient
                .post<MessageResponse>(Endpoints.urlMessagesSend) {
                    parameter("token", accessToken)
                    message.let {
                        parameter("message", it.message)
                        parameter("iduseremitter", it.idUserEmitter)
                        parameter("iduserreceiver", it.idUserReceiver)
                    }
                }
            response.data
        } catch (t: Throwable) {
            return when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }

    suspend fun getMessages(accessToken: String?, message: Message): List<Message>? {
        Log.i("mensajes", "getMessages -->")
        try {
            Log.i("mensajes", "getMessages --> ${message.idUserEmitter}")
            val response = httpClient.get<MessageResponse>(Endpoints.urlMessages) {
                //parameter("token", accessToken)
                parameter("iduseremitter", message.idUserEmitter)
            }
            Log.i("mensajes", "getMessages --> ${response.data}")
            return response.data
        } catch (t: Throwable) {
            return null
            /* when (t) {
                 is ClientRequestException -> {
                     if (t.response?.status?.value == 401) {
                         throw UnauthorizedException
                     }
                     null
                 }
                 else -> null
             }*/
            return null
        }
    }

    suspend fun getMyMessages(accessToken: String?, message: Message): List<Message>? {
        return try {
            val response = httpClient.get<MessageResponse>(Endpoints.urlMyMessages) {
                parameter("token", accessToken)
                message.let {
                    parameter("iduseremitter", it.idUserEmitter)
                    parameter("iduserreceiver", it.idUserReceiver)
                }
            }
            response.data
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }
/*
    suspend fun getChat(accessToken: String?, message: Message) {
        try {
            val response = httpClient.webSocket(Endpoints.messagesMyMessages) {
                send("You are connected!")
                for(frame in incoming) {
                    frame as? Frame.Text
                    continue
                    val receivedText = frame.readText()
                    send("You said: $receivedText")
                }
            }
            response
        } catch (t: Throwable) {
            when (t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    null
                }
                else -> null
            }
        }
    }*/

}