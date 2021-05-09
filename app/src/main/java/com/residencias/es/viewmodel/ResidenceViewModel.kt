package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.user.User
import com.residencias.es.data.user.UserRepository
import com.residencias.es.utils.Resource
import kotlinx.coroutines.launch


class ResidenceViewModel(
        private val repository: UserRepository,
        private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _user = MutableLiveData<Resource<User?>>()
    val user: LiveData<Resource<User?>>
        get() = _user

    fun getUserProfile() {
        viewModelScope.launch {
            _user.postValue(Resource.loading(null))

            repository.getUser(authenticationRepository.getAccessToken())?.let { response ->
                // Success :)
                _user.postValue(Resource.success(response))
            } ?: run {
                // Failure :(
                _user.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }

    fun updateUserDescription(description: String) {
        viewModelScope.launch {
            _user.postValue(Resource.loading(null))

         /*   repository.updateUser(description)?.let { response ->
                // Success :)
                _user.postValue(Resource.success(response))
            } ?: run {
                // Failure :(
                _user.postValue(Resource.error(R.string.error_profile.toString(), null))
            }*/
        }
    }

    fun logout() {
        authenticationRepository.logout()
    }

    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}