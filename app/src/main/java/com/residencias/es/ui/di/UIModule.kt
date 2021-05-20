package com.residencias.es.ui.di

import com.residencias.es.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { SplashViewModel(repository = get()) }
    viewModel { RegisterViewModel(repository = get()) }
    viewModel { LoginViewModel(repository = get()) }
    viewModel { MainViewModel( authenticationRepository = get()) }
    viewModel { ResidencesViewModel(repository = get(), authenticationRepository = get()) }
    viewModel { ResidenceViewModel(repository = get(), authenticationRepository = get()) }
    viewModel { ProfileViewModel(repository = get(), authenticationRepository = get()) }
    viewModel { MyResidenceViewModel(repository = get(), authenticationRepository = get()) }
    viewModel { ResidencesSearchViewModel(repository = get(), authenticationRepository = get()) }
    viewModel { PhotoViewModel(repository = get(), authenticationRepository = get()) }
}