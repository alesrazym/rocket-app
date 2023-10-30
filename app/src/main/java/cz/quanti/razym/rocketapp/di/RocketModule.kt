package cz.quanti.razym.rocketapp.di

import cz.quanti.razym.rocketapp.data.RocketsRepositoryImpl
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val rocketModule = module {
    singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }
    viewModelOf(::RocketListViewModel)
}
