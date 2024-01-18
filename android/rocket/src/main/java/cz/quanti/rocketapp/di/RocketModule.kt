package cz.quanti.rocketapp.di

import cz.quanti.rocketapp.presentation.RocketDetailViewModel
import cz.quanti.rocketapp.presentation.RocketListViewModel
import cz.quanti.rocketapp.system.provideNavOptions
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val rocketModule = module {
    single { provideNavOptions() }

    viewModelOf(::RocketListViewModel)
    viewModelOf(::RocketDetailViewModel)
}
