package cz.quanti.razym.rocketapp.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.quanti.razym.rocketapp.data.RocketsRepositoryImpl
import cz.quanti.razym.rocketapp.data.SpaceXApi
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val rocketModule = module {
    single { provideMoshi() }
    single { provideConverterFactory(get()) }
    single { provideRetrofit(get()) }
    single { provideSpaceXApi(get()) }
    singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }
    viewModelOf(::RocketListViewModel)
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
}

fun provideConverterFactory(moshi: Moshi): Converter.Factory {
    return MoshiConverterFactory.create(moshi)
}

fun provideRetrofit(converter: Converter.Factory): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.spacexdata.com/")
        .addConverterFactory(converter)
        .build()
}

fun provideSpaceXApi(retrofit: Retrofit): SpaceXApi = retrofit.create(SpaceXApi::class.java)

