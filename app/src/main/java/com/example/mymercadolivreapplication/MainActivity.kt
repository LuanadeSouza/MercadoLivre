package com.example.mymercadolivreapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mymercadolivreapplication.navigation.MercadoLivreNavigation
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the application.
 * This is the entry point for the Android app and initializes the app UI and navigation.
 *
 * Why use @AndroidEntryPoint?
 * - Integrates Hilt with the Activity lifecycle.
 * - Allows Hilt to inject dependencies directly into the Activity (if needed).
 * - This is the entry point for the Hilt dependency injection graph in Android.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is created.
     * This is where the content of the activity is set, and the navigation system is configured.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // Initializes the splash screen for the app
        super.onCreate(savedInstanceState)

        // Set the content view using Jetpack Compose
        setContent {
            // Apply the app's theme
            MyMercadoLivreApplicationTheme {
                // Configure the navigation system of the application
                MercadoLivreNavigation()
            }
        }
    }
}