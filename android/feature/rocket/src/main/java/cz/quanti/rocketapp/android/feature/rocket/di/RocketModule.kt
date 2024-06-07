package cz.quanti.rocketapp.android.feature.rocket.di

import cz.quanti.rocketapp.android.feature.rocket.presentation.RocketDetailViewModel
import cz.quanti.rocketapp.android.feature.rocket.presentation.RocketListViewModel
import cz.quanti.rocketapp.android.lib.uisystem.system.provideNavOptions
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val rocketModule = module {
    single { provideNavOptions() }

    viewModelOf(::RocketListViewModel)
    viewModelOf(::RocketDetailViewModel)
}
