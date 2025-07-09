package com.example.mymercadolivreapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymercadolivreapplication.ui.detail.ProductDetailScreen
import com.example.mymercadolivreapplication.ui.result.SearchResultsScreen
import com.example.mymercadolivreapplication.ui.search.SearchScreen
import com.example.mymercadolivreapplication.ui.splash.SplashScreen
import com.example.mymercadolivreapplication.utils.FirebaseAnalyticsManager

/**
 * Main navigation graph of the application using Jetpack Navigation Compose.
 *
 * Responsibilities:
 * - Defines the structure and routing of screens within the app.
 * - Associates composable destinations with specific route strings.
 * - Passes required parameters and dependencies (e.g., FirebaseAnalyticsManager).
 *
 * Routes:
 * - "splash"   -> Initial splash screen.
 * - "search"   -> Search screen with user input.
 * - "results/{query}" -> Results screen showing items based on search term.
 * - "detail/{productId}" -> Detailed view of a selected product.
 *
 * @param navController Controller that handles navigation actions and back stack.
 * @param analyticsManager Manager used to track screen views and interactions.
 */
@Composable
fun MercadoLivreNavigation(
    navController: NavHostController = rememberNavController(),
    analyticsManager: FirebaseAnalyticsManager
) {
    NavHost(
        navController = navController,
        startDestination = "splash" // Launch screen when the app starts
    ) {
        // Splash Screen route
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("search") {
                    popUpTo("splash") {
                        inclusive = true
                    }
                }
            })
        }

        // Search Results Screen route
        // Accepts the search term as a path argument
        composable("search") {
            SearchScreen(
                navController = navController,
                analyticsManager = analyticsManager
            )
        }

        // Search Results Screen route
        // {query} is a navigation argument containing the search term
        composable("results/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(
                navController = navController,
                query = query
            )
        }

        // Product Detail Screen route
        // {productId} is a navigation argument containing the product ID
        composable("detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                navController = navController,
                productId = productId
            )
        }
    }
}