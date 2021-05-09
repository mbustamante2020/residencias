package com.residencias.es.data.di

import com.residencias.es.data.SessionManager
import com.residencias.es.data.datasource.ApiDataSource
import com.residencias.es.data.message.MessageRepository
import com.residencias.es.data.message.MessageRepositoryImpl
import com.residencias.es.data.network.Network
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.oauth.AuthenticationRepositoryImpl
import com.residencias.es.data.residences.ResidencesRepository
import com.residencias.es.data.residences.ResidencesRepositoryImpl
import com.residencias.es.data.user.UserRepository
import com.residencias.es.data.user.UserRepositoryImpl
import org.koin.dsl.module

val dataModule = module {

    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            ApiDataSource(Network.createHttpClient(get())),
            SessionManager(get())
        ) }

    single<ResidencesRepository> {
        ResidencesRepositoryImpl(
            ApiDataSource(Network.createHttpClient(get())),
            SessionManager(get())
        ) }

    single<MessageRepository> {
        MessageRepositoryImpl(
                ApiDataSource(Network.createHttpClient(get())),
                SessionManager(get())
        ) }

    single<UserRepository> {  UserRepositoryImpl(ApiDataSource(Network.createHttpClient(get()))) }

}