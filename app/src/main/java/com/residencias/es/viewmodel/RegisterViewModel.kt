package com.residencias.es.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.residencias.es.R
import com.residencias.es.data.oauth.OAuthRepository
import com.residencias.es.data.oauth.model.OAuthToken
import com.residencias.es.utils.Resource


// This is a simple ViewModel example,
// you can connect to it from the LaunchActivity and use it
// or just remove it
class RegisterViewModel(
    private val oAuthRepository: OAuthRepository
) : ViewModel() {

    private val _getToken = MutableLiveData<Resource<OAuthToken>>()
    val getOAuthTokens: LiveData<Resource<OAuthToken>>
        get() = _getToken

    suspend fun register(name: String, email: String, password: String) {
        _getToken.postValue(Resource.loading(null))

        oAuthRepository.register(name, email, password)?.let { response ->
            // Success :)
            response.accessToken.let {
                oAuthRepository.saveAccessToken(it)
            }
            oAuthRepository.saveUserData(response.id, response.name, response.email, response.role)

            Log.i("login", "vm ${response.accessToken}")

            _getToken.postValue(Resource.success(response))
        } ?: run {
            // Failure :(
            _getToken.postValue(Resource.error(R.string.error_oauth.toString(), null))
        }
    }
}