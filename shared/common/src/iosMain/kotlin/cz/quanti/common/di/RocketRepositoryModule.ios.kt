package cz.quanti.common.di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

internal actual fun provideClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(Darwin) {
        config()

        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }
