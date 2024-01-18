package cz.quanti.rocketropository.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.check.checkModules
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class CheckModulesTest {
    @Test
    fun verifyModules() {
        rocketRepositoryModule.verify()
    }

    @Test
    fun checkModules() {
        checkModules {
            modules(rocketRepositoryModule)
        }
    }
}
