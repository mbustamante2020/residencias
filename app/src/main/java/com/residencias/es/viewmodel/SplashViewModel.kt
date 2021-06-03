package com.residencias.es.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.data.oauth.OAuthRepository
import kotlinx.coroutines.launch

class SplashViewModel(
        private val oAuthRepository: OAuthRepository
) : ViewModel() {

    val isUserAvailable = MutableLiveData<Boolean>()

    fun getUserAvailability() {
        viewModelScope.launch {
            isUserAvailable.postValue(oAuthRepository.isUserAvailable())
        }
    }
}