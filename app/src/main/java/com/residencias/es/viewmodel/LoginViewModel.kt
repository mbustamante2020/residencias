package com.residencias.es.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.residencias.es.R
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.oauth.OAuthTokensResponse
import com.residencias.es.utils.Resource


class LoginViewModel(
        private val repository: AuthenticationRepository
) : ViewModel() {

    private val _getToken = MutableLiveData<Resource<OAuthTokensResponse>>()
    val getOAuthTokens: LiveData<Resource<OAuthTokensResponse>>
        get() = _getToken

    suspend fun login(email: String, password: String) {
        _getToken.postValue(Resource.loading(null))

        repository.login(email, password)?.let { response ->
            // Success :)
            response.accessToken.let {
                repository.saveAccessToken(it)
            }
            repository.saveUserData(response.id, response.name, response.email, response.role)

            Log.i("login", "vm ${response.accessToken}");

            _getToken.postValue(Resource.success(response))
        } ?: run {
            // Failure :(
            _getToken.postValue(Resource.error(R.string.error_oauth.toString(), null))
        }
    }

    suspend fun loginGoogle(email: String, name: String) {
        _getToken.postValue(Resource.loading(null))

        repository.loginGoogle(email, name)?.let { response ->
            // Success :)
            response.accessToken.let {
                repository.saveAccessToken(it)
            }
            repository.saveUserData(response.id, response.name, response.email, response.role)

            Log.i("login", "vm ${response.accessToken}");

            _getToken.postValue(Resource.success(response))
        } ?: run {
            // Failure :(
            _getToken.postValue(Resource.error(R.string.error_oauth.toString(), null))
        }
    }
}