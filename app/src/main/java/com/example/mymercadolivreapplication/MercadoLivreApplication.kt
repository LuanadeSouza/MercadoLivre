package com.example.mymercadolivreapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application class for the MercadoLivre application.
 * This class is used to initialize the application and configure global settings.
 *
 * Why use this class?
 * - This is the main entry point for the app's initialization.
 * - Used for setting up global libraries like Timber for logging.
 * - Initializes dependency injection with Hilt.
 */
@HiltAndroidApp
class MercadoLivreApplication : Application() {

    /**
     * Called when the application is created.
     * Sets up global settings like logging.
     */
    override fun onCreate() {
        super.onCreate()

        // Initializes Timber for logging if the app is in debug mode
        if (applicationContext.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            Timber.plant(Timber.DebugTree()) // Planting Timber's DebugTree for logging in debug builds
        }
    }
}