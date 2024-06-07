package cz.quanti.rocketapp.multiplatform.lib.common.di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module
import kotlinx.serialization.json.Json

val commonModule =
    module {
        single { provideJson() }
        single { provideEngine() }
        single { provideClientConfig(get()) }
        single { provideClient(get(), get()) }
    }

private fun provideJson(): Json {
    return Json {
        // From mobile-assignment-kmm
        prettyPrint = true
        isLenient = true

        ignoreUnknownKeys = true
        useAlternativeNames = true
    }
}

// Provides already configured client engine.
internal expect fun provideEngine(): HttpClientEngine

private fun provideClient(engine: HttpClientEngine, config: HttpClientConfig<*>): HttpClient =
    HttpClient(engine, config)

private fun provideClientConfig(json: Json): HttpClientConfig<*> {
    return HttpClientConfig<HttpClientEngineConfig>().apply {
        installConfig(json)

        // TODO: Do we want base url, as we will have separate client for each source,
        //  or to use full url in requests?

        /*
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.spacexdata.com/v4"
                port = 443
            }
        }
         */
    }
}

private fun HttpClientConfig<*>.installConfig(json: Json) {
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 5_000
        // As of 01/2024 Darwin engine does not support connect timeout.
        // See https://ktor.io/docs/timeout.html#limitations
        connectTimeoutMillis = 1_000
    }
}
