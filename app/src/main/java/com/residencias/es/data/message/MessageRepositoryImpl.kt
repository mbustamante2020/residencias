package com.residencias.es.data.message

import com.residencias.es.data.SessionManager
import com.residencias.es.data.datasource.ApiDataSource


class MessageRepositoryImpl(
        private val apiDataSource: ApiDataSource,
        private val sharedPreferencesDataSource: SessionManager
) : MessageRepository {

    override suspend fun sendMessage(message: Message): List<Message>? {
        return apiDataSource.sendMessage(sharedPreferencesDataSource.getAccessToken(), message)
    }

    override suspend fun getMessages(message: Message): List<Message>? {
        return apiDataSource.getMessages(sharedPreferencesDataSource.getAccessToken(), message)
    }

    override suspend fun getMyMessages(message: Message): List<Message>? {
        return apiDataSource.getMyMessages(sharedPreferencesDataSource.getAccessToken(), message)
    }

    override fun onUnauthorized() {
    }
}