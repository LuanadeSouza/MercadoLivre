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
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the appl
 * ;ç.;^^^~~~ication.
 * This is the entry point for the Android app and initializes the app UI and navigation.
 *
 * Why use @AndroidEntryPoint?
 * - Integrates Hilt with the Activity lifecycle.
 * - Allows Hilt to inject dependencies directly into the Activity (if needed).
 * - This is the entry point for the Hilt dependency injection graph in Android.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen() // Initializes the splash screen for the app
        setContent {
            MyMercadoLivreApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMercadoLivreApplicationTheme {
        Greeting("Android")
    }
}