package cz.quanti.rocketapp.multiplatform.feature.rocket.di

import cz.quanti.rocketapp.multiplatform.feature.rocket.data.RocketsRepositoryImpl
import cz.quanti.rocketapp.multiplatform.feature.rocket.data.SpaceXApi
import cz.quanti.rocketapp.multiplatform.feature.rocket.data.SpaceXApiImpl
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.GetRocketUseCase
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.GetRocketUseCaseImpl
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.GetRocketsUseCase
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.GetRocketsUseCaseImpl
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.RocketsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val rocketRepositoryModule =
    module {
        singleOf(::SpaceXApiImpl) { bind<SpaceXApi>() }
        singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }

        factoryOf(::GetRocketsUseCaseImpl) { bind<GetRocketsUseCase>() }
        factoryOf(::GetRocketUseCaseImpl) { bind<GetRocketUseCase>() }
    }
