package cz.quanti.razym.rocketropository.di

import cz.quanti.razym.rocketropository.data.RocketsRepositoryImpl
import cz.quanti.razym.rocketropository.data.SpaceXApi
import cz.quanti.razym.rocketropository.data.SpaceXApiImpl
import cz.quanti.razym.rocketropository.domain.GetRocketUseCase
import cz.quanti.razym.rocketropository.domain.GetRocketsUseCase
import cz.quanti.razym.rocketropository.domain.RocketsRepository
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlinx.serialization.json.Json

val rocketRepositoryModule =
    module {
        single { provideJson() }
        single { provideClientConfig(get()) }
        single { provideClient(get()) }
        singleOf(::SpaceXApiImpl) { bind<SpaceXApi>() }
        singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }
        factory { GetRocketsUseCase { get<RocketsRepository>().getRockets() } }
        factory { GetRocketUseCase { get<RocketsRepository>().getRocket(it) } }
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

private fun provideClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    // Use default engine, as per https://ktor.io/docs/http-client-engines.html#default
    return HttpClient {
        config()
    }
}

private fun provideClientConfig(json: Json): HttpClientConfig<*>.() -> Unit {
    return {
        installConfig(json)
    }
}

private fun HttpClientConfig<*>.installConfig(json: Json) {
    install(ContentNegotiation) {
        json(json)
    }
}
