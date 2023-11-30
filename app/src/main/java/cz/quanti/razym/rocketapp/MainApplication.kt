package cz.quanti.razym.rocketapp

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import cz.quanti.razym.rocketapp.di.rocketModule
import cz.quanti.razym.rocketropository.Greeting
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

private const val MaxMemoryCacheSizePercent = 0.25
private const val MaxDiskCacheSizePercent = 0.02

class MainApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        Log.w("MainApplication", Greeting().greet())

        startKoin {
            // Reference Android context
            androidContext(this@MainApplication)

            androidLogger(Level.DEBUG)

            modules(rocketModule)
        }
    }

    // Create cached image loader, using sample values from doc.
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(MaxMemoryCacheSizePercent)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(MaxDiskCacheSizePercent)
                    .build()
            }
            .build()
}
