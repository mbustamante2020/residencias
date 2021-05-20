package com.residencias.es.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.photo.Photo
import com.residencias.es.data.photo.PhotoRepository
import com.residencias.es.utils.Resource
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import java.io.File


class PhotoViewModel(
    private val repository: PhotoRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
/*
    private val _uploadPhotos = MutableLiveData<Resource<HttpResponse?>>()
    val uploadPhotos: LiveData<Resource<HttpResponse?>>
        get() = _uploadPhotos

    fun uploadPhoto(file: File, photo: Photo) {
        viewModelScope.launch {
            _uploadPhotos.postValue(Resource.loading(null))

            repository.uploadImage(file, photo).let {
                // Success :)
                //_uploadPhotos.postValue(Resource.success(it))
            } ?: run {
                // Failure :(
                _uploadPhotos.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }*/


    private val _photos2 = MutableLiveData<Resource<HttpResponse?>>()
    val photos2: LiveData<Resource<HttpResponse?>>
        get() = _photos2

    fun uploadPhoto(file: File?, photo: Photo) {
        viewModelScope.launch {
            _photos2.postValue(Resource.loading(null))

            file?.let {
                repository.uploadImage(it, photo).let {
                    // Success :)
                    //  _photos2.postValue(Resource.success(it))
                }
            } ?: run {
                // Failure :(
                _photos2.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }





















    private val _photos = MutableLiveData<Resource<Pair<Int?, List<Photo>?>>>()
    val photos: LiveData<Resource<Pair<Int?, List<Photo>?>>>
        get() = _photos

    val editImage = MutableLiveData<Boolean>()
    var photo  = MutableLiveData<Photo>()

    suspend fun getImages() {
        _photos.postValue(Resource.loading(null))

        Log.i("ResidencesViewModel", "32")
        repository.getImages().let {
            if (it != null) {
                Log.i("ResidencesViewModel", "34 ${it.second}")
            }
            it?.let {
                Log.i("ResidencesViewModel", "36")
                _photos.postValue(Resource.success(it))
                Log.i("ResidencesViewModel", "38")
            } ?: run {
                _photos.postValue(Resource.error(R.string.error_residences.toString(), null))

            }
        }
    }

    fun  photoClicked(photoObj: Photo?) {
        viewModelScope.launch {
            photo.postValue(photoObj!!)
            editImage.postValue(true)
        }
    }





//////////////////////////////////////
    private val _photos1 = MutableLiveData<Resource<Photo?>>()
    val photos1: LiveData<Resource<Photo?>>
        get() = _photos1

    fun updatePhoto(photo: Photo) {
        viewModelScope.launch {
            _photos1.postValue(Resource.loading(null))

            repository.updateImage(photo)?.let {
                // Success :)
                _photos1.postValue(Resource.success(it))
            } ?: run {
                // Failure :(
                _photos1.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            _photos1.postValue(Resource.loading(null))

            repository.deleteImage(photo)?.let {
                // Success :)
                _photos1.postValue(Resource.success(it))
            } ?: run {
                // Failure :(
                _photos1.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }

    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}