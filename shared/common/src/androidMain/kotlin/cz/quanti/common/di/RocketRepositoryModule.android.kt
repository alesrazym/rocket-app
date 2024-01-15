package cz.quanti.common.di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android

internal actual fun provideClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(Android) {
        config()
    }
