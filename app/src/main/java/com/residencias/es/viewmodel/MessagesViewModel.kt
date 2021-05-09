package com.residencias.es.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.residencias.es.R
import com.residencias.es.data.message.Message
import com.residencias.es.data.message.MessageRepository
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.utils.Resource
import kotlinx.coroutines.launch


class MessagesViewModel(
    private val repository: MessageRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    
    private val _messages = MutableLiveData<Resource<List<Message>?>>()
    val messages: LiveData<Resource<List<Message>?>>
        get() = _messages

    val viewMessage = MutableLiveData<Boolean>()
    var message  = MutableLiveData<Message>()
        //get() = message

    suspend fun getMessages(message: Message) {
        _messages.postValue(Resource.loading(null))

        repository.getMessages(message).let { messages ->
            //_messages.postValue(Resource.success(messages))
         //   _messages.postValue(Resource.success(messages))
            messages?.let {
                _messages.postValue(Resource.success(messages))

            } ?: run {
                _messages.postValue(Resource.error(R.string.error.toString(), null))

            }


        }
    }

    fun messageClicked(message1: Message?) {
        viewModelScope.launch {
            message.postValue(message1)
            viewMessage.postValue(true)
        }
    }

    fun onUnauthorized() {
        authenticationRepository.onUnauthorized()
    }
}