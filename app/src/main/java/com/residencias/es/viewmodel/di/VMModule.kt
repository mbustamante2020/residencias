package com.residencias.es.viewmodel.di

import com.residencias.es.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { SplashViewModel(oAuthRepository = get()) }
    viewModel { RegisterViewModel(oAuthRepository = get()) }
    viewModel { LoginViewModel(oAuthRepository = get()) }
    viewModel { MainViewModel( oAuthRepository = get()) }
    viewModel { ResidencesViewModel(residencesRepository = get()) }
    viewModel { ProfileViewModel(userRepository = get()) }
    viewModel { MyResidenceViewModel(residencesRepository = get()) }
    viewModel { ResidencesSearchViewModel(residencesRepository = get()) }
    viewModel { PhotoViewModel(photoRepository = get()) }
}