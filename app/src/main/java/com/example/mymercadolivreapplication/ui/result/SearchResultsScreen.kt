package com.example.mymercadolivreapplication.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
import com.example.mymercadolivreapplication.ui.theme.DarkGray
import com.example.mymercadolivreapplication.ui.theme.GreenCustom
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.YellowCustom
import com.example.mymercadolivreapplication.ui.theme.Typography
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
            .width(150.dp)
            .height(300.dp)
            .clickable(onClick = onClick)
            .background(Color.White)
            .semantics {
                contentDescription = "Produto: ${product.title}, Preço: ${
                    formatPrice(
                        product.price,
                        product.currencyId
                    )
                }"
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = product.thumbnail,
                contentDescription = stringResource(id = R.string.product_details) + ": ${product.title}",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.title,
                    style = Typography.labelSmall,
                    maxLines = 2,
                    modifier = Modifier.semantics {
                        contentDescription = "Título do produto: ${product.title}"
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPrice(product.price, product.currencyId),
                    style = Typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.semantics {
                        contentDescription =
                            "Preço do produto: ${formatPrice(product.price, product.currencyId)}"
                    }              )
                if (product.shipping?.freeShipping == true) {
                    Text(
                        text = stringResource(id = R.string.free_shipping),
                        style = Typography.labelSmall,
                        color = GreenCustom,
                        modifier = Modifier.semantics {
                            contentDescription = "Frete grátis disponível para o produto"
                        }
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
                thumbnail = "@drawable/ic_launcher_background",
                condition = "new",
                shipping = Shipping(freeShipping = true),
                seller = null,
                attributes = null
            )
        ) {}
    }
}