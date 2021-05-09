package com.residencias.es.data.message


interface MessageRepository {

    suspend fun sendMessage(message: Message): List<Message>?

    suspend fun getMessages(message: Message): List<Message>?

    suspend fun getMyMessages(message: Message): List<Message>?

    fun onUnauthorized()
}