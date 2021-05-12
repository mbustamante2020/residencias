package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.residences.ResidencesRepository
import com.residencias.es.utils.Resource
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import java.io.File


class PhotoViewModel(
    private val repository: ResidencesRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _photos = MutableLiveData<Resource<HttpResponse?>>()
    val photos: LiveData<Resource<HttpResponse?>>
        get() = _photos

    fun updatePhoto(file: File, headerValue: String) {
        viewModelScope.launch {
            _photos.postValue(Resource.loading(null))

            repository.uploadFile(authenticationRepository.getAccessToken(), file, headerValue)?.let { response ->
                // Success :)
              //  _photos.postValue(Resource.success(response))
            } ?: run {
                // Failure :(
                _photos.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }















    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}