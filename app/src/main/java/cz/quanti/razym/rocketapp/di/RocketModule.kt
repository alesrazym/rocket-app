package cz.quanti.razym.rocketapp.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.quanti.razym.rocketapp.data.RocketsRepositoryImpl
import cz.quanti.razym.rocketapp.data.SpaceXApi
import cz.quanti.razym.rocketapp.domain.GetRocketsUseCase
import cz.quanti.razym.rocketapp.domain.GetRocketsUseCaseImpl
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val rocketModule = module {
    single { provideClient() }
    single { provideMoshi() }
    single { provideConverterFactory(get()) }
    single { provideRetrofit(get(), get()) }
    single { provideSpaceXApi(get()) }
    singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }
    factoryOf(::GetRocketsUseCaseImpl) { bind<GetRocketsUseCase>() }
    viewModelOf(::RocketListViewModel)
}

private fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
}

private fun provideConverterFactory(moshi: Moshi): Converter.Factory {
    return MoshiConverterFactory.create(moshi)
}

private fun provideClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .callTimeout(5, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(client: OkHttpClient, converter: Converter.Factory): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.spacexdata.com/")
        .client(client)
        .addConverterFactory(converter)
        .build()
}

private fun provideSpaceXApi(retrofit: Retrofit): SpaceXApi = retrofit.create(SpaceXApi::class.java)

