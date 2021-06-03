package com.residencias.es.data.di

import com.residencias.es.data.network.Network
import com.residencias.es.data.oauth.OAuthRepository
import com.residencias.es.data.oauth.OAuthRepositoryImpl
import com.residencias.es.data.oauth.datasource.OauthApiDataSource
import com.residencias.es.data.oauth.datasource.SessionManager
import com.residencias.es.data.photo.PhotoRepository
import com.residencias.es.data.photo.PhotoRepositoryImpl
import com.residencias.es.data.photo.datasource.PhotoApiDataSource
import com.residencias.es.data.residence.ResidencesRepository
import com.residencias.es.data.residence.ResidencesRepositoryImpl
import com.residencias.es.data.residence.datasource.ResidenceApiDataSource
import com.residencias.es.data.user.UserRepository
import com.residencias.es.data.user.UserRepositoryImpl
import com.residencias.es.data.user.datasource.UserApiDataSource
import org.koin.dsl.module

val dataModule = module {

    single<OAuthRepository> {
        OAuthRepositoryImpl(
            OauthApiDataSource(Network.createHttpClient(get())), SessionManager(get())
        ) }

    single<ResidencesRepository> {
        ResidencesRepositoryImpl(
            ResidenceApiDataSource(Network.createHttpClient(get())), SessionManager(get())
        ) }

    single<PhotoRepository> {
        PhotoRepositoryImpl(
                PhotoApiDataSource(Network.createHttpClient(get())), SessionManager(get())
        ) }

    single<UserRepository> {  UserRepositoryImpl(UserApiDataSource(Network.createHttpClient(get()))) }

}