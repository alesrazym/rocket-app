package cz.quanti.rocketropository.di

import cz.quanti.rocketropository.asResult
import cz.quanti.rocketropository.data.RocketsRepositoryImpl
import cz.quanti.rocketropository.data.SpaceXApi
import cz.quanti.rocketropository.data.SpaceXApiImpl
import cz.quanti.rocketropository.domain.GetRocketUseCase
import cz.quanti.rocketropository.domain.GetRocketsUseCase
import cz.quanti.rocketropository.domain.RocketsRepository
import cz.quanti.rocketropository.model.asRocket
import cz.quanti.rocketropository.model.asRocketDetail
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val rocketRepositoryModule =
    module {
        single { provideJson() }
        single { provideClientConfig(get()) }
        single { provideClient(get()) }
        singleOf(::SpaceXApiImpl) { bind<SpaceXApi>() }
        singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }

        factory {
            GetRocketsUseCase {
                get<RocketsRepository>().getRockets().map { list ->
                    list.map { it.asRocket() }
                }.asResult()
            }
        }

        factory {
            GetRocketUseCase {
                get<RocketsRepository>().getRocket(it).map { data ->
                    data.asRocketDetail()
                }.asResult()
            }
        }
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
        // TODO: Add timeouts and check cancellation support.

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
