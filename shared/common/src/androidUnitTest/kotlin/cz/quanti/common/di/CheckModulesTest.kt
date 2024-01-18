package cz.quanti.common.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.check.checkModules
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class CheckModulesTest {
    @Test
    fun verifyModules() {
        commonModule.verify()
    }

    @Test
    fun checkModules() {
        checkModules {
            modules(commonModule)
        }
    }
}
