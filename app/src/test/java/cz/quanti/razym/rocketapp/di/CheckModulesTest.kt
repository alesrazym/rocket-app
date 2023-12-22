package cz.quanti.razym.rocketapp.di

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
                    cz.quanti.razym.rocketropository.domain.RocketsRepository::class,
                    cz.quanti.razym.rocketropository.domain.GetRocketsUseCase::class,
                    cz.quanti.razym.rocketropository.domain.GetRocketUseCase::class,
                ),
        )
    }
}
