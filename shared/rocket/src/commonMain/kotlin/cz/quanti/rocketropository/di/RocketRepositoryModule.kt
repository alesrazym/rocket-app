package cz.quanti.rocketropository.di

import cz.quanti.common.di.commonModule
import cz.quanti.rocketropository.data.RocketsRepositoryImpl
import cz.quanti.rocketropository.data.SpaceXApi
import cz.quanti.rocketropository.data.SpaceXApiImpl
import cz.quanti.rocketropository.domain.GetRocketUseCase
import cz.quanti.rocketropository.domain.GetRocketUseCaseImpl
import cz.quanti.rocketropository.domain.GetRocketsUseCase
import cz.quanti.rocketropository.domain.GetRocketsUseCaseImpl
import cz.quanti.rocketropository.domain.RocketsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val rocketRepositoryModule =
    module {
        includes(commonModule)

        singleOf(::SpaceXApiImpl) { bind<SpaceXApi>() }
        singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }

        factoryOf(::GetRocketsUseCaseImpl) { bind<GetRocketsUseCase>() }
        factoryOf(::GetRocketUseCaseImpl) { bind<GetRocketUseCase>() }
    }
