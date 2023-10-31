package cz.quanti.razym.rocketapp

import cz.quanti.razym.rocketapp.di.rocketModule
import org.junit.Test
import org.koin.test.verify.verify

class CheckModulesTest {
    @Test
    fun checkAllModules() {
        rocketModule.verify()
    }
}