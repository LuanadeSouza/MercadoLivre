package com.example.mymercadolivreapplication.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.YellowCustom
import kotlinx.coroutines.delay

/**
 * Splash Screen.
 * Displayed at the start of the app to load resources or simulate a loading time.
 *
 * Why use a splash screen?
 * - Enhances the user experience by filling the initial loading time.
 * - Allows displaying the app's branding.
 * - Can be used to initialize resources in the background.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Launches a coroutine to display the splash screen for a set time
    LaunchedEffect(Unit) {
        // Simulate loading delay
        delay(2000L) // 2-second delay
        onTimeout() // Calls the function when the delay is over
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowCustom), // Background color set to YellowCustom
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Displays the logo image of the app
        Image(
            modifier = Modifier
                .fillMaxSize(), // Image takes up the entire screen
            painter = painterResource(id = R.drawable.mqdefault), // Default launcher icon
            contentDescription = stringResource(id = R.string.image_description_logo), // Describes the image for accessibility
        )
    }
}

/**
 * Preview of the com.example.mymercadolivreapplication.ui.splash.SplashScreen for UI design and layout validation.
 * Displays the splash screen with the logo during the preview.
 */
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MyMercadoLivreApplicationTheme {
        SplashScreen(onTimeout = {})
    }
}