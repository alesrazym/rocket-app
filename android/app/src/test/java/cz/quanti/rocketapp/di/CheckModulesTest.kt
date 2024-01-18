package cz.quanti.rocketapp.di

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.check.checkModules
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class CheckModulesTest {
    @Test
    fun verifyModules() {
        appModule.verify()
    }

    @Test
    fun checkModules() {
        checkModules {
            modules(appModule)
        }
    }
}
