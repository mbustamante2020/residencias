package com.residencias.es.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.data.oauth.AuthenticationRepository
import kotlinx.coroutines.launch

class SplashViewModel(
        private val repository: AuthenticationRepository
) : ViewModel() {

    val isUserAvailable = MutableLiveData<Boolean>()

    fun getUserAvailability() {
        viewModelScope.launch {
            isUserAvailable.postValue(repository.isUserAvailable())
        }
    }
}