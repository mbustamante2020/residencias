package com.residencias.es.data.photo

import com.residencias.es.data.oauth.datasource.SessionManager
import com.residencias.es.data.photo.datasource.PhotoApiDataSource
import com.residencias.es.data.photo.model.Photo
import java.io.File


class PhotoRepositoryImpl(
    private val apiDataSource: PhotoApiDataSource,
    private val localDataSource: SessionManager
) : PhotoRepository {

    override suspend fun getImages(): Pair<Int?, List<Photo>?>? {
        return apiDataSource.getImages()
    }

    override suspend fun updateImage(photo: Photo?): Photo? {
        return apiDataSource.updateImage(photo)
    }

    override suspend fun deleteImage(photo: Photo?): Photo? {
        return apiDataSource.deleteImage(photo)
    }

    override suspend fun uploadImage(file: File, photo: Photo) {
        apiDataSource.uploadImage(localDataSource.getAccessToken(), file, photo)
    }

    override suspend fun onUnauthorized() {
        localDataSource.clearAccessToken()
    }
}