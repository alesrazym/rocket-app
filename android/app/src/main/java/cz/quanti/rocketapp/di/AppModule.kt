package cz.quanti.rocketapp.di

import cz.quanti.common.di.commonModule
import cz.quanti.rocketropository.di.rocketRepositoryModule
import org.koin.dsl.module

val appModule = module {
    includes(commonModule, rocketRepositoryModule, rocketModule)
}
