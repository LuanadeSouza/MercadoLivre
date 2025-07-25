package com.example.mymercadolivreapplication.ui.result

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.ui.result.component.ProductItem
import com.example.mymercadolivreapplication.ui.search.SearchViewModel
import com.example.mymercadolivreapplication.ui.theme.DarkGray
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import com.example.mymercadolivreapplication.ui.theme.YellowCustom
import com.example.mymercadolivreapplication.utils.FirebaseAnalyticsManager
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Screen that displays the search results for a given query.
 *
 * Responsibilities:
 * - Fetches and displays a list of products based on a search term.
 * - Handles UI states: loading, error, no results, and list of results.
 * - Supports navigation to product details.
 * - Logs screen view and search result analytics events.
 *
 * @param navController Controller used to handle navigation actions.
 * @param query The term entered by the user to search products.
 * @param analyticsManager Manager used for tracking analytics events.
 * @param viewModel ViewModel responsible for managing the search logic and state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    query: String,
    analyticsManager: FirebaseAnalyticsManager = FirebaseAnalyticsManager(
        context = androidx.compose.ui.platform.LocalContext.current
    ),
    viewModel: SearchViewModel = hiltViewModel()
) {
    // Collecting the UI state from the ViewModel
    val viewState by viewModel.viewState.collectAsState()

    // Trigger search and analytics logging on screen composition
    LaunchedEffect(query) {
        viewModel.searchProducts(query)

        analyticsManager.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "search_results_screen")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, "SearchResultsScreen")
            }
        )

        analyticsManager.logSearchEvent(query)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Resultados para \"$query\"",
                        style = Typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = "Resultados para a pesquisa: $query"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            analyticsManager.logEvent(
                                "click_back_from_results",
                                Bundle().apply {
                                    putString("origin", "SearchResultsScreen")
                                }
                            )
                            navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = YellowCustom,
                    titleContentColor = DarkGray,
                    navigationIconContentColor = DarkGray
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                viewState.isLoading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.loading),
                        modifier = Modifier.semantics {
                            contentDescription = "Carregando resultados da pesquisa..."
                        }
                    )
                }

                viewState.errorMessage != null -> {
                    Text(
                        text = viewState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = Typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription =
                                "Erro ao carregar os resultados: ${viewState.errorMessage!!}"
                        }
                    )
                }

                viewState.showNoResults -> {
                    NoResultsScreen()
                }

                viewState.products.isNotEmpty() -> {
                    // LazyColumn for displaying the list of products efficiently
                    // Why LazyColumn?
                    // - Renders only visible items on the screen, optimizing performance for large lists.
                    // - Recycles components, saving memory.
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(viewState.products) { product ->
                            ProductItem(product = product) {
                                analyticsManager.logEvent(
                                    "click_product_from_results",
                                    Bundle().apply {
                                        putString("product_id", product.id)
                                        putString("product_title", product.title)
                                        putString("from_screen", "SearchResultsScreen")
                                    }
                                )

                                // Navigates to the product details screen
                                navController.navigate("detail/${product.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultsScreenPreview() {
    MyMercadoLivreApplicationTheme {
        SearchResultsScreen(
            navController = rememberNavController(),
            query = "celular"
        )
    }
}