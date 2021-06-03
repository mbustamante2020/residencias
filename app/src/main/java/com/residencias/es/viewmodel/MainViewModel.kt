package com.residencias.es.viewmodel

import androidx.lifecycle.ViewModel
import com.residencias.es.data.oauth.OAuthRepository
import com.residencias.es.utils.OAuthConstants


class MainViewModel(
        private val oAuthRepository: OAuthRepository
) : ViewModel() {

    fun logout() {
        oAuthRepository.logout()
    }

    fun isResidence(): Boolean {
        return oAuthRepository.getRole() == OAuthConstants.roleResidence
    }

}