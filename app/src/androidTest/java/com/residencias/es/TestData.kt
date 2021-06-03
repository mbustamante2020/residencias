package com.residencias.es

import android.content.Context
import com.residencias.es.data.network.Network
import com.residencias.es.data.oauth.datasource.OauthApiDataSource
import com.residencias.es.data.oauth.datasource.SessionManager
import com.residencias.es.data.residence.datasource.ResidenceApiDataSource
import io.ktor.client.*


object TestData {
    const val networkWaitingMillis = 5000L
    const val sharedPrefsWaitingMillis = 500L

    // Network
    private fun provideHttpClient(context: Context): HttpClient = Network.createHttpClient(context)
    fun provideOauthApiService(context: Context): OauthApiDataSource = OauthApiDataSource(provideHttpClient(context))
    fun provideResidenceApiService(context: Context): ResidenceApiDataSource = ResidenceApiDataSource(provideHttpClient(context))
    fun provideOauthLocalService(context: Context): SessionManager = SessionManager(context)

    var email = "mbustama1@uoc.edu"
    var password = "clave2021*"
}