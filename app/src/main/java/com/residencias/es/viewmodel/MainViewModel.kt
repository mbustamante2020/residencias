package com.residencias.es.viewmodel

import androidx.lifecycle.ViewModel
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.user.UserRepository


class MainViewModel(
        private val repository: UserRepository,
        private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun logout() {
        authenticationRepository.logout()
    }

}