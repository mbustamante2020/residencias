package com.residencias.es.viewmodel

import androidx.lifecycle.ViewModel
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.oauth.Constants


class MainViewModel(
        private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun logout() {
        authenticationRepository.logout()
    }

    fun isResidence(): Boolean {
        return authenticationRepository.getRole() == Constants.roleResidence
    }

}