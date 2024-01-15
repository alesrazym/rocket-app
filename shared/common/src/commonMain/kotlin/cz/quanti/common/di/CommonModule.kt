package cz.quanti.common.di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module
import kotlinx.serialization.json.Json

val commonModule =
    module {
        single { provideJson() }
        single { provideClientConfig(get()) }
        single { provideClient(get()) }
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

internal expect fun provideClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

private fun provideClientConfig(json: Json): HttpClientConfig<*>.() -> Unit {
    return {
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
