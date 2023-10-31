package cz.quanti.razym.rocketapp

import android.app.Application
import cz.quanti.razym.rocketapp.di.rocketModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {

            // Reference Android context
            androidContext(this@MainApplication)

            androidLogger(Level.DEBUG)

            modules(rocketModule)
        }
    }
}