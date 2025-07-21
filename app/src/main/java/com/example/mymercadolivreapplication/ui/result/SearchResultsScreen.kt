package com.example.mymercadolivreapplication.ui.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.data.model.Product
import com.example.mymercadolivreapplication.data.model.Shipping
import com.example.mymercadolivreapplication.ui.search.SearchViewModel
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import java.text.NumberFormat
import java.util.Locale

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
                        modifier = Modifier.semantics {
                            contentDescription = "Resultados para a pesquisa: $query"
                        } // contentDescription for title
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = "Erro ao carregar os resultados: ${viewState.errorMessage!!}"
                        }
                    )
                }

                viewState.showNoResults -> {
                    Text(
                        text = stringResource(id = R.string.no_results),
                        style = MaterialTheme.typography.bodyLarge,
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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

/**
 * Component for a product item in the results list.
 *
 * Why Card?
 * - Provides an elevated surface to group related content.
 * - Enhances the visual and tactile experience.
 *
 * Why GlideImage?
 * - Optimized library for loading and displaying images on Android.
 * - Manages caching, resizing, and placeholders automatically.
 *
 * Accessibility:
 * - `contentDescription` for the product image for screen readers.
 * - Price formatting using `NumberFormat` to ensure correct localization.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = "Produto: ${product.title}, Preço: ${formatPrice(product.price, product.currencyId)}"
            }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = product.thumbnail,
                contentDescription = stringResource(id = R.string.product_details) + ": ${product.title}",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics {
                        contentDescription = "Título do produto: ${product.title}"
                    } // contentDescription for product title
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPrice(product.price, product.currencyId),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.semantics {
                        contentDescription = "Preço do produto: ${formatPrice(product.price, product.currencyId)}"
                    } // contentDescription for price
                )
                if (product.shipping?.freeShipping == true) {
                    Text(
                        text = stringResource(id = R.string.free_shipping),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.semantics {
                            contentDescription = "Frete grátis disponível para o produto"
                        } // contentDescription for free shipping
                    )
                }
            }
        }
    }
}

/**
 * Utility function to format price according to the currency and locale.
 *
 * Why use NumberFormat?
 * - Ensures correct formatting of monetary values for different regions.
 * - Automatically handles currency symbols, thousand separators, and decimals.
 * - Essential for accessibility and internationalization.
 */
fun formatPrice(price: Double, currencyId: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR")) // Example for BRL
    format.currency = java.util.Currency.getInstance(currencyId)
    return format.format(price)
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

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    MyMercadoLivreApplicationTheme {
        ProductItem(
            product = Product(
                id = "MLA123",
                title = "Celular Samsung Galaxy S23 Ultra 5G 256GB",
                price = 5999.99,
                currencyId = "BRL",
                availableQuantity = 10,
                thumbnail = "https://http2.mlstatic.com/D_NQ_NP_2X_686906-MLA54967397940_042023-F.webp",
                condition = "new",
                shipping = Shipping(freeShipping = true),
                seller = null,
                attributes = null
            )
        ) {}
    }
}