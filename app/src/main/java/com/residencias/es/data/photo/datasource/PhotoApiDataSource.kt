package com.residencias.es.data.photo.datasource

import android.util.Log
import android.webkit.MimeTypeMap
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.photo.model.Photo
import com.residencias.es.data.photo.model.PhotoResponse
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

class PhotoApiDataSource(private val httpClient: HttpClient )  {

    private val tag: String = "PhotoRemoteDataSource"

    fun uploadImage(accessToken: String?, sourceFile: File, photo: Photo) {
            Thread {
                val mimeType = getMimeType(sourceFile)
                if (mimeType == null) {
                    Log.e(tag, "file error Not able to get mime type")
                    return@Thread
                }
                val uploadedFileName = "image.jpg"
                val fileName: String = uploadedFileName

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

                    if (response.isSuccessful) {
                        Log.d(tag,"success, path: $fileName $response")
                        //showToast("File uploaded successfully at $serverUploadDirectoryPath$fileName")
                    } else {
                        Log.e(tag, "failed $response")
                        //showToast("File uploading failed")
                    }
                    response.close()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.e(tag, "failed")
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

    suspend fun getImages(): Pair<Int?, List<Photo>?>? {
        return try {
            val response = httpClient.get<PhotoResponse>(Endpoints.urlGetImages)
            Pair(response.image_count, response.data)
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    suspend fun updateImage(photo: Photo?): Photo?{
        return try {
            httpClient.put<Photo>(Endpoints.urlUpdateImage) {
                photo?.let {
                    parameter("id", it.id)
                    parameter("title", it.title)
                    parameter("description", it.description)
                    parameter("principal", it.principal)
                }
            }
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }

    suspend fun deleteImage(photo: Photo?): Photo?{
        return try {
            httpClient.delete<Photo>(Endpoints.urlDeleteImage) {
                photo?.let {
                    parameter("id", it.id)
                }
            }
        } catch (t: Throwable) {
            Log.w(tag, "Error Getting Access token", t)
            null
        }
    }
}