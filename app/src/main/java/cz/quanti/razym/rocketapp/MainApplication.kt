package cz.quanti.razym.rocketapp

import android.app.Application
import cz.quanti.razym.rocketapp.data.RocketsRepoImpl
import cz.quanti.razym.rocketapp.domain.RocketsRepo
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {

            // Reference Android context
            androidContext(this@MainApplication)

            androidLogger(Level.DEBUG)

            modules(
                module {
                    single<RocketsRepo> { RocketsRepoImpl() }
                    viewModelOf(::RocketListViewModel)
                }
            )
        }
    }
}