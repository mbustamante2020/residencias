package com.residencias.es.data.di

import com.residencias.es.data.datasource.ApiDataSource
import com.residencias.es.data.datasource.SessionManager
import com.residencias.es.data.network.Network
import com.residencias.es.data.oauth.AuthenticationRepository
import com.residencias.es.data.oauth.AuthenticationRepositoryImpl
import com.residencias.es.data.photo.PhotoRemoteDataSource
import com.residencias.es.data.photo.PhotoRepository
import com.residencias.es.data.photo.PhotoRepositoryImpl
import com.residencias.es.data.residence.ResidencesRepository
import com.residencias.es.data.residence.ResidencesRepositoryImpl
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

    single<PhotoRepository> {
        PhotoRepositoryImpl(
                PhotoRemoteDataSource(Network.createHttpClient(get())),
                SessionManager(get())
        ) }

    single<UserRepository> {  UserRepositoryImpl(ApiDataSource(Network.createHttpClient(get()))) }

}