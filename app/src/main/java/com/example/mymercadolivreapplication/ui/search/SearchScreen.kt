package com.example.mymercadolivreapplication.ui.search

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.ui.theme.DarkGray
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import com.example.mymercadolivreapplication.ui.theme.YellowCustom
import com.example.mymercadolivreapplication.utils.FirebaseAnalyticsManager
import com.google.firebase.analytics.FirebaseAnalytics


/**
 * Composable screen responsible for allowing the user to input a search query
 * and navigate to the results screen.
 *
 * Key responsibilities:
 * - Renders a top app bar with the screen title.
 * - Provides a search input field restricted to a single line.
 * - Submits the query to navigate to the results when the button is pressed.
 * - Triggers a Firebase Analytics screen_view event when the screen is composed.
 * - Applies accessibility content descriptions for screen readers.
 *
 * @param navController Controller used to navigate between screens.
 * @param analyticsManager Manager for logging events to Firebase Analytics.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    analyticsManager: FirebaseAnalyticsManager
) {

    // Strings for accessibility and semantics
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val titleTopBarDescription = stringResource(R.string.search)
    val searchFieldDescription = stringResource(id = R.string.search_field_label)
    val searchButtonDescription = stringResource(id = R.string.search_button_description)

    // Logs screen view to Firebase Analytics on first composition
    LaunchedEffect(Unit) {
        analyticsManager.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "search_screen")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, "SearchScreen")
            })
    }

    // Main layout scaffold with top bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titleTopBarDescription,
                        style = Typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = titleTopBarDescription
                        })
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = YellowCustom, titleContentColor = DarkGray
                )
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Text input for search, restricted to a single line
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(text = stringResource(id = R.string.search_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = searchFieldDescription },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button that navigates to the results screen with the typed query
            Button(
                onClick = {
                    if (searchQuery.text.isNotBlank()) {
                        navController.navigate("results/${searchQuery.text}")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = searchQuery.text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = YellowCustom, contentColor = DarkGray
                )
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.search_button),
                    modifier = Modifier.semantics {
                        contentDescription = searchButtonDescription
                    })
            }
        }
    }
}

/**
 * Preview function for rendering the SearchScreen in Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MyMercadoLivreApplicationTheme {
        SearchScreen(
            navController = rememberNavController(),
            analyticsManager = FirebaseAnalyticsManager(context = androidx.compose.ui.platform.LocalContext.current)
        )
    }
}