package com.residencias.es.data

import android.util.Log
import com.residencias.es.data.message.Message
import com.residencias.es.data.message.MessageResponse
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.oauth.TokenResponse
import com.residencias.es.data.residences.*
import com.residencias.es.data.user.User
import com.residencias.es.data.user.UserResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.request.get
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ApiService(private val httpClient: HttpClient) {

    val TAG: String = "ApiService"

    /*********** ACCESO ************/
    suspend fun login(email: String, password: String): TokenResponse? {
        return try {
            httpClient.post<TokenResponse>(Endpoints.login) {
                parameter("email", email)
                parameter("password", password)

                /*body = Json {
                    "email" to email
                    "password" to password
                }*/

            //"{ 'email' : '$email', 'password' : '$password' }"
           /* body = MultiPartFormDataContent(
                        formData {
                            append("email", email)
                            append("password", password)
                        }
                )*/

               /* body = formData {
                            append("email", email)
                            append("password", password)
                        }*/

                //header("X-Requested-With", "XMLHttpRequest")
            }

        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            null
        }
    }

    suspend fun register(name: String, email: String, password: String): TokenResponse? {
        return try {
            httpClient.post<TokenResponse>(Endpoints.register) {
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
            val response = httpClient.get<UserResponse>(Endpoints.user){
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
                    .put<UserResponse>(Endpoints.updateUser) {
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
                val response = httpClient.get<ResidenceResponse>(Endpoints.residence){
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
                    .put<ResidenceResponse>(Endpoints.updateResidence) {
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

                            //"${binding.sectors}",

                            //"${binding.province}",
                            //"${binding.town}",
                            //"image",
                            parameter("price", it.price)
                            parameter("description", it.shortDescription)
                            parameter("latitude", it.latitude)
                            parameter("longitude", it.longitude)

                            parameter("idtown", it.idtown)

                            //"${binding.province}",
                            //"${binding.town}",

                            /*
        $residencia->estado = ( empty($request->input('estado')) )?2:$request->input('estado');     //int
        $residencia->residencia_vip = $request->input('residencia_vip');       //on
        $residencia->edad_minima_entrar = $request->input('edad_minima_entrar');                    //int
        $residencia->respiro_familiar = ($request->input('respiro_familiar') == "on")?true:false;   //on
        $residencia->plazas_libres = ($request->input('plazas_libres') == "on")?true:false;         //on
        $residencia->tamano_residencia = $request->input('tamano_residencia');                      //int
        $residencia->municipios_id = $request->input('municipio');      //int
        $residencia->codigo_postal = $request->input('codigo_postal');  //texto
                             */


















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
    suspend fun getMyResidenceRooms(accessToken: String?): RoomResponse? {
        return try {
            httpClient.get<RoomResponse>("${Endpoints.residenceRooms}") {
                parameter("token", accessToken)
            }
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
    suspend fun getMyResidenceSectors(accessToken: String?): SectorResponse? {
        return try {
            httpClient.get<SectorResponse>("${Endpoints.residenceSectors}") {
                parameter("token", accessToken)
            }
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
    suspend fun getMyResidenceDependences(accessToken: String?): DependenceResponse? {
        return try {
            httpClient.get<DependenceResponse>("${Endpoints.residenceDependences}") {
                parameter("token", accessToken)
            }
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

//659
    @Throws(UnauthorizedException::class)
    suspend fun uploadFile(accessToken: String?, file: ByteArray, headerValue: String) = withContext(Dispatchers.IO) {
        try {


//
         //   file1.serializer()


         //   var image: Photo = Photo("nombre", file1)



                // Prepare Input Stream
                val inputStream = file.inputStream().asInput()
                // Create Form Data
                val parts: List<PartData> = formData {
                    // Optional Headers
                    append("headerKey", headerValue)
                    // File Input Stream
                    appendInput("image", size = file.size.toLong()) { inputStream }
                }
                // Launch Request
                val response = httpClient.submitFormWithBinaryData<HttpResponse>("${Endpoints.residenceUploadImage}?token=$accessToken", parts)
                // Close Input Stream
                inputStream.close()






          //  var mapa = mutableMapOf<String, ByteArray>()
           // mapa["imagen"] = file
          //  uploadFilesTest( accessToken,  mapa)




/*
            val resp = httpClient.post<String>("${Endpoints.residenceUploadImage}?token=$accessToken") {
                /*body = MultiPartFormDataContent(formData {
                    append("file", "123")
                })
                */
                body = MultiPartFormDataContent(formData {
                    append("image", file, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, ContentDisposition.File.withParameter(ContentDisposition.Parameters.FileName, "res.jpg"))
                    })
                })


            }
*/




/*
            val parts: List<PartData> = formData {
                // Regular form parameter
                append("text", "Hello, world")

                // File upload. Param name is "file-1" and file's name is "file.csv"
                append("file-1", "file.csv", ContentType.Text.CSV) {
                    this.append("1,2,3")
                }

                // Verbose DSL
                val headersBuilder = HeadersBuilder()
                headersBuilder[HttpHeaders.ContentType] = "application/java-archive"
                headersBuilder[HttpHeaders.ContentDisposition] = "filename=wrapper.jar"
                this.append(
                        "image",
                        InputProvider { file.inputStream().asInput() },
                        headersBuilder.build()
                )
            }

            httpClient.submitFormWithBinaryData<Unit>(formData = parts /* prepared parts */) {
                url("${Endpoints.residenceUploadImage}?token=$accessToken")

                // Query string parameters
                parameter("param-1", "value-1")
                parameter("param-2", "value-2-1")
                parameter("param-2", "value-2-2")

                // Headers
                headers {
                    this["X-My-Header-1"] = "X-My-Header-1-Value"
                    appendAll("X-My-Header-2", listOf("X-My-Header-2-Value-1", "X-My-Header-2-Value-2"))
                }
            }
*/





/*
            val multipart = httpClient.receiveMultipart()
            multipart.forEachPart { part ->
                // if part is a file (could be form item)
                if(part is PartData.FileItem) {
                    // retrieve file name of upload
                    val name = part.originalFileName!!
                    val file = File("/uploads/$name")

                    // use InputStream from part to save file
                    part.streamProvider().use { its ->
                        // copy the stream to the file with buffering
                        file.outputStream().buffered().use {
                            // note that this is blocking
                            its.copyTo(it)
                        }
                    }
                }
                // make sure to dispose of the part after use to prevent leaks
                part.dispose()
            }
*/



        } catch (t: Throwable) {
            Log.i("observePhoto", "Error $t")
            // Handle Error
        }
        //return null
    }


/*
    suspend fun uploadFiles( binaryFiles: Map<String,ByteArray> ): BaseResponse<List<String>> {
        return httpClient.submitForm {

            url(fileUploadUrl)
            method = HttpMethod.Post
            body = MultiPartFormDataContent(
                    formData {
                        headers{
                            append("Content-Type", "application/json")
                            append("Authorization", "Bearer $token}")
                        }
                        binaryFiles.entries.forEach {
                            append(
                                    key = "files",
                                    value = it.value,
                                    headers = Headers.build {
                                        append(HttpHeaders.ContentDisposition, "filename=${it.key}")
                                    }
                            )
                        }
                    }
            )
        }
    }

*/

/*
    suspend fun uploadFilesTest(
            accessToken: String?,
            binaryFiles: Map<String,ByteArray>
    ) {
        httpClient.post<String>("${Endpoints.residenceUploadImage}?token=$accessToken") {
            headers {
                append("Content-Type", ContentType.Application.Json)
                append("Authorization", "Bearer $accessToken")
            }
            body = MultiPartFormDataContent(
                    formData {
                        binaryFiles.entries.forEach {
                            this.appendInput(
                                    key = "files",
                                    size = it.value.size.toLong(),
                                    headers = Headers.build {
                                        append(HttpHeaders.ContentDisposition, "filename=${it.key}")
                                    },
                            ){ buildPacket { writeFully(it.value) }}
                        }
                    }
            )
        }
    }*/
suspend fun uploadFilesTest( accessToken: String?, binaryFiles: Map<String,ByteArray> ) {
    httpClient.post<String>("${Endpoints.residenceUploadImage}?token=$accessToken") {
        headers {
            append("Content-Type", ContentType.Application.Json)
            append("Authorization", "Bearer $accessToken")
        }
        body = MultiPartFormDataContent(
                formData {
                    binaryFiles.entries.forEach {
                        this.appendInput(
                                key = "image",
                                size = it.value.size.toLong(),
                                headers = Headers.build {
                                    append(HttpHeaders.ContentDisposition, "filename=${it.key}")
                                },
                        ){ buildPacket { writeFully(it.value) }}
                    }
                }
        )
    }
}

















    /*********** RESIDENCIA ************/
    @Throws(UnauthorizedException::class)
    suspend fun getResidence(accessToken: String?): Residence? {
        return try {
            val response = httpClient.get<ResidenceResponse>(Endpoints.residence){
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
    suspend fun updateResidence(accessToken: String?, residence: Residence?): Residence? {
        try {
            val response = httpClient
                .put<ResidenceResponse>(Endpoints.updateResidence) {
                    parameter("token", accessToken)
                    residence?.let {
                        parameter("nombre", it.name)
                        parameter("direccion", it.address)
                        parameter("municipios_id", it.town)
                        parameter("descripcion_resumen", it.shortDescription)
                        parameter("email", it.email)
                        parameter("telefono", it.phone)
                        parameter("web", it.web)
                        parameter("latitud", it.latitude)
                        parameter("longitud", it.longitude)
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


    /*********** RESIDENCIAS ************/
    // Se obtienen las Residences
    @Throws(UnauthorizedException::class)
    suspend fun getResidences(pagina: Int? = null, search: Search? = null): ResidencesResponse? {
        Log.i("current_page", "apiservice 42 $pagina")
        return try {
            httpClient.get<ResidencesResponse>(Endpoints.residencesUrl) {
                pagina?.let {
                    parameter("page", pagina)
                }
                search?.let { it ->
                    it.search_for?.let { b ->
                        parameter("buscar_por", b)
                    }
                    it.province?.let { b ->
                        parameter("provincia", b)
                    }
                    it.town?.let { b ->
                        parameter("municipio", b)
                    }
                    it.price?.let { b ->
                        parameter("precio", b)
                    }
                    it.size_residence?.let { b ->
                        parameter("tamano", b)
                    }
                    it.room?.let { b ->
                        parameter("habitacion", b)
                    }
                    it.sector?.let { b ->
                        parameter("plaza", b)
                    }
                    it.dependence?.let { b ->
                        parameter("dependencia", b)
                    }
                    it.services?.let { b ->
                        parameter("servicio", b)
                    }
                    it.family_respite?.let { b ->
                        parameter("respiro", b)
                    }
                    it.sectors_available?.let { b ->
                        parameter("plazas", b)
                    }
                    it.under_65?.let { b ->
                        parameter("menor", b)
                    }
                }
            }
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
    suspend fun getProvinces(all: Boolean?): ProvinceResponse? {
        return try {
            httpClient.get<ProvinceResponse>(Endpoints.provinciasUrl) {
                all?.let { parameter("todas", all)  }
            }
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
    suspend fun getTowns(idprovince: Int? = null, all: Boolean?): TownResponse? {
        return try {
            httpClient.get<TownResponse>("${Endpoints.townsUrl}/$idprovince") {
                all?.let { parameter("todas", all) }
            }
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
    suspend fun getRooms(): RoomResponse? {
        return try {
            httpClient.get<RoomResponse>("${Endpoints.roomsUrl}")
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
    suspend fun getSectors(): SectorResponse? {
        return try {
            httpClient.get<SectorResponse>("${Endpoints.sectorsUrl}")
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
    suspend fun getDependences(): DependenceResponse? {
        return try {
            httpClient.get<DependenceResponse>("${Endpoints.dependencesUrl}")
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
                    .put<MessageResponse>(Endpoints.messagesSend) {
                        parameter("token", accessToken)
                        message?.let {
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
            val response = httpClient.get<MessageResponse>(Endpoints.messages) {
                //parameter("token", accessToken)
                message?.let {
                    parameter("iduseremitter", it.idUserEmitter)
                }
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
            val response = httpClient.get<MessageResponse>(Endpoints.messagesMyMessages) {
                parameter("token", accessToken)
                message?.let {
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