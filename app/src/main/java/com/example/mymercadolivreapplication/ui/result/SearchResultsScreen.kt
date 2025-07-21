package com.example.mymercadolivreapplication.ui.result

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

/**
 * Product search results screen.
 * Displays a list of found products and allows navigation to the product details screen.
 *
 * Why Scaffold?
 * - Provides a basic structure for Material Design screens (TopAppBar, FloatingActionButton, etc.).
 * - Manages content padding, ensuring that it is not overlapped by the top bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    query: String,
    viewModel: SearchViewModel = hiltViewModel()
) {
    // Collecting the UI state from the ViewModel
    val viewState by viewModel.viewState.collectAsState()

    // Trigger search when the screen is composed for the first time or when the query changes
    LaunchedEffect(query) {
        viewModel.searchProducts(query)
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
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    Text(
                        text = stringResource(id = R.string.no_results),
                        style = Typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = "Nenhum resultado encontrado para a pesquisa."
                        }
                    )
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