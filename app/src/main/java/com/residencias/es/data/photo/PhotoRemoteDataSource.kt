package com.residencias.es.data.photo

import android.util.Log
import android.webkit.MimeTypeMap
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.residence.*
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

class PhotoRemoteDataSource(private val httpClient: HttpClient )  {

    private val tag: String = "PhotoRemoteDataSource"

    @Throws(UnauthorizedException::class)
    fun uploadImage(accessToken: String?, sourceFile: File, photo: Photo) {
            Thread {
                val mimeType = getMimeType(sourceFile);
                if (mimeType == null) {
                    Log.e("file error", "Not able to get mime type")
                    return@Thread
                }
                var uploadedFileName = "imagen.jpg"
                val fileName: String = uploadedFileName ?: sourceFile.name

                try {
                    val requestBody: RequestBody =
                        MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("image", fileName, sourceFile.asRequestBody(mimeType.toMediaTypeOrNull()))
                            .addFormDataPart("title", "${photo.title}")
                            .addFormDataPart("description", "${photo.description}")
                            .addFormDataPart("principal", "${photo.principal}")
                            .addFormDataPart("token", accessToken.toString())
                            .build()

                    val request: Request = Request.Builder().url(Endpoints.urlUploadImage).post(requestBody).build()

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
    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }



    @Throws(UnauthorizedException::class)
    suspend fun getImages(): Pair<Int?, List<Photo>?>? {
        return try {
            val response = httpClient.get<PhotoResponse>("${Endpoints.urlGetImages}")
            Pair(response.image_count, response.data)
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
    suspend fun updateImage(photo: Photo?): Photo?{
        return try {
            Log.i("updateImage", "${photo?.id} ${photo?.title}")


            httpClient.put<Photo>(Endpoints.urlUpdateImage) {
                photo?.let {
                    parameter("id", it.id)
                    parameter("title", it.title)
                    parameter("description", it.description)
                    parameter("principal", it.principal)
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

    @Throws(UnauthorizedException::class)
    suspend fun deleteImage(photo: Photo?): Photo?{
        return try {
            httpClient.delete<Photo>(Endpoints.urlDeleteImage) {
                photo?.let {
                    parameter("id", it.id)
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
}