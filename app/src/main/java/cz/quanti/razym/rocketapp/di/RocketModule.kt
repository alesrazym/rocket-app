package cz.quanti.razym.rocketapp.di

import androidx.navigation.navOptions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.data.RocketsRepositoryImpl
import cz.quanti.razym.rocketapp.data.SpaceXApi
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.RocketDetailViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
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
    single { provideNavOptions() }
    singleOf(::RocketsRepositoryImpl) { bind<RocketsRepository>() }
    viewModelOf(::RocketListViewModel)
    viewModelOf(::RocketDetailViewModel)
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

private fun provideNavOptions() = navOptions {
    anim {
        enter = R.anim.slide_in
        exit = R.anim.fade_out
        popEnter = R.anim.fade_in
        popExit = R.anim.slide_out
    }
}
