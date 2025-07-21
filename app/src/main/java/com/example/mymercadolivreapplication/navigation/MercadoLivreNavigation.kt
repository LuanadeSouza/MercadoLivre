package com.example.mymercadolivreapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymercadolivreapplication.ui.ProductDetailScreen
import com.example.mymercadolivreapplication.ui.SearchResultsScreen
import com.example.mymercadolivreapplication.ui.splash.SplashScreen
import com.example.mymercadolivreapplication.ui.SearchScreen

/**
 * Navigation system for the application using Navigation Compose.
 *
 * Why use Navigation Compose?
 * - Native integration with Jetpack Compose: declarative navigation that aligns with the Compose philosophy.
 * - Type-safe navigation: reduces runtime navigation errors.
 * - Automatic management of the navigation stack (back stack).
 * - Safe handling of navigation arguments.
 * - Integration with ViewModel scoping: ViewModels can be shared between destinations.
 * - Support for deep links and conditional navigation.
 *
 * Navigation Structure:
 * 1. "search" -> Search screen
 * 2. "results/{query}" -> Results screen with search term as an argument
 * 3. "detail/{productId}" -> Product details screen with product ID as an argument
 */
@Composable
fun MercadoLivreNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash" // The app starts at the Splash Screen
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(onTimeout = { navController.navigate("search") { popUpTo("splash") { inclusive = true } } })
        }

        // Search Screen
        composable("search") {
            SearchScreen(navController = navController)
        }

        // Search Results Screen
        // {query} is a navigation argument containing the search term
        composable("results/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(
                navController = navController,
                query = query
            )
        }

        // Product Details Screen
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

/**
 * Object that centralizes the navigation routes of the application.
 *
 * Why centralize routes?
 * - Avoids hardcoded strings scattered throughout the code.
 * - Makes maintenance and route changes easier.
 * - Reduces typos in routes.
 * - Improves code readability and organization.
 * - Allows safer refactoring.
 */
object MercadoLibreDestinations {
    const val SEARCH = "search"
    const val RESULTS = "results"
    const val DETAIL = "detail"

    /**
     * Builds the route for the results screen with the search term.
     *
     * @param query The search term
     * @return Formatted route for navigation
     */
    fun resultsRoute(query: String): String = "$RESULTS/$query"

    /**
     * Builds the route for the details screen with the product ID.
     *
     * @param productId The product ID
     * @return Formatted route for navigation
     */
    fun detailRoute(productId: String): String = "$DETAIL/$productId"
}