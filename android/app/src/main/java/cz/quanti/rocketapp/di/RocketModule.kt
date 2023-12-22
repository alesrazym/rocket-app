package cz.quanti.rocketapp.di

import androidx.navigation.navOptions
import cz.quanti.rocketapp.R
import cz.quanti.rocketapp.presentation.RocketDetailViewModel
import cz.quanti.rocketapp.presentation.RocketListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val rocketModule = module {
    single { provideNavOptions() }
    viewModelOf(::RocketListViewModel)
    viewModelOf(::RocketDetailViewModel)
}

private fun provideNavOptions() = navOptions {
    anim {
        enter = R.anim.slide_in
        exit = R.anim.fade_out
        popEnter = R.anim.fade_in
        popExit = R.anim.slide_out
    }
}
