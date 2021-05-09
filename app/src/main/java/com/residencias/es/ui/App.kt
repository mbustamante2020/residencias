package com.residencias.es.ui

import android.app.Application
import com.residencias.es.data.di.dataModule
import com.residencias.es.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, uiModule))
        }
    }
}