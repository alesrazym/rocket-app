package cz.quanti.rocketapp.di

import cz.quanti.rocketropository.domain.GetRocketUseCase
import cz.quanti.rocketropository.domain.GetRocketsUseCase
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class CheckModulesTest {
    @Test
    fun checkAllModules() {
        rocketModule.verify(
            extraTypes =
                listOf(
                    GetRocketsUseCase::class,
                    GetRocketUseCase::class,
                ),
        )
    }
}
