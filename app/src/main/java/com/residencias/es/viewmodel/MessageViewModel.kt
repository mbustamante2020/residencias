package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.residencias.es.R
import com.residencias.es.data.message.Message
import com.residencias.es.data.message.MessageRepository
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.utils.Resource


class MessageViewModel(
    private val repository: MessageRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {


    private val _messages = MutableLiveData<Resource<List<Message>?>>()
    val messages: LiveData<Resource<List<Message>?>>
        get() = _messages

    val viewMessage = MutableLiveData<Boolean>()
    var message  = MutableLiveData<Message>()

    suspend fun getMyMessages(message: Message) {
        _messages.postValue(Resource.loading(null))

        repository.getMyMessages(message).let { messages ->

            messages?.let {
                _messages.postValue(Resource.success(messages))

            } ?: run {
                _messages.postValue(Resource.error(R.string.error.toString(), null))

            }
        }
    }

    suspend fun sendMessageClicked(sendMessage: Message) {
        _messages.postValue(Resource.loading(null))

        repository.sendMessage(sendMessage).let { messages ->

            messages?.let {
                _messages.postValue(Resource.success(messages))

            } ?: run {
                _messages.postValue(Resource.error(R.string.error.toString(), null))

            }
        }
    }


    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}