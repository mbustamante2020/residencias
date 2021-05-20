package com.residencias.es.data.photo

import java.io.File


interface PhotoRepository {

    suspend fun getImages(): Pair<Int?, List<Photo>?>?

    suspend fun updateImage(photo: Photo?): Photo?

    suspend fun deleteImage(photo: Photo?): Photo?

    suspend fun uploadImage(file: File, photo: Photo)

    suspend fun onUnauthorized()
}