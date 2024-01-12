package cz.quanti.common.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class CheckModulesTest {
    @Test
    fun checkAllModules() {
        commonModule.verify(
            // As long as we use default engine,
            // we are unable to specify neither the engine nor the config, are we?
            extraTypes =
                listOf(
                    io.ktor.client.engine.HttpClientEngine::class,
                    io.ktor.client.HttpClientConfig::class,
                ),
        )
    }
}
