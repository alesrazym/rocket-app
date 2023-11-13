package cz.quanti.razym.rocketapp.di

import org.junit.Test
import org.koin.test.verify.verify

class CheckModulesTest {
    @Test
    fun checkAllModules() {
        rocketModule.verify()
    }
}