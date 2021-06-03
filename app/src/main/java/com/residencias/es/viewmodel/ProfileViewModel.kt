package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.user.User
import com.residencias.es.data.user.UserRepository
import com.residencias.es.utils.Resource
import kotlinx.coroutines.launch


class ProfileViewModel(
        private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<Resource<User?>>()
    val user: LiveData<Resource<User?>>
        get() = _user

    fun getUser() {
        viewModelScope.launch {
            _user.postValue(Resource.loading(null))

            userRepository.getUser()?.let { response ->
                // Success :)
                _user.postValue(Resource.success(response))
            } ?: run {
                // Failure :(
                _user.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }

    fun updateUser(user: User?) {
        viewModelScope.launch {
            _user.postValue(Resource.loading(null))

            userRepository.updateUser(user)?.let { response ->
                // Success :)
                _user.postValue(Resource.success(response))
            } ?: run {
                // Failure :(
                _user.postValue(Resource.error(R.string.error_profile.toString(), null))
            }
        }
    }
}