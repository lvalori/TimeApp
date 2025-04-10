package com.lvalori.timeapp

import android.app.Application
import android.os.StrictMode
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TimeApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        // Inizializza Timber per il logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            // Abilita StrictMode in debug
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )

            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }

        // Altre inizializzazioni che potrebbero essere necessarie
        setupExceptionHandler()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .components {
                add(SvgDecoder.Factory())
            }
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }

    private fun setupExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "Uncaught exception in thread ${thread.name}")
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    companion object {
        const val DATABASE_NAME = "timeapp_database"
        const val DATABASE_VERSION = 1

        // Costanti per le preferenze
        const val PREFS_NAME = "timeapp_preferences"
        const val PREF_DARK_MODE = "dark_mode"
        const val PREF_LAST_SYNC = "last_sync"

        // Altre costanti dell'applicazione
        const val MIN_PASSWORD_LENGTH = 6
        const val MAX_SESSION_DURATION_HOURS = 24
        const val SYNC_INTERVAL_HOURS = 24
    }
}