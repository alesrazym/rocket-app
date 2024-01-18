package cz.quanti.common.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

internal actual fun provideEngine(): HttpClientEngine = Android.create()
