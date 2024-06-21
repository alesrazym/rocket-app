package cz.quanti.rocketapp.android.app.di

import cz.quanti.rocketapp.android.feature.rocket.di.rocketModule
import cz.quanti.rocketapp.multiplatform.feature.rocket.di.rocketRepositoryModule
import cz.quanti.rocketapp.multiplatform.lib.common.di.commonModule
import org.koin.dsl.module

val appModule = module {
    includes(commonModule, rocketRepositoryModule, rocketModule)
}
