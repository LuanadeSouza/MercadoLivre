package com.example.mymercadolivreapplication.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.data.model.Attribute
import com.example.mymercadolivreapplication.data.model.Picture
import com.example.mymercadolivreapplication.data.model.ProductDetail
import com.example.mymercadolivreapplication.data.model.Shipping
import com.example.mymercadolivreapplication.ui.result.formatPrice
import com.example.mymercadolivreapplication.ui.theme.DarkGray
import com.example.mymercadolivreapplication.ui.theme.GreenCustom
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.YellowCustom
import java.util.Locale

/**
 * Product details screen.
 * Displays detailed information about a specific product.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    // Collecting the UI state from the ViewModel
    val viewState by viewModel.viewState.collectAsState()

    // Trigger loading of product details when the screen is composed
    LaunchedEffect(productId) {
        viewModel.loadProductDetail(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val productDetailDescription = stringResource(id = R.string.product_details)
                    Text(
                        text = productDetailDescription,
                        modifier = Modifier.semantics {
                            contentDescription = productDetailDescription
                        }
                    )

                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.clearProductDetail()
                            navController.popBackStack()
                        }) {
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
                .verticalScroll(rememberScrollState()), // Allows scrolling for long content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                viewState.isLoading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.loading))
                }

                viewState.errorMessage != null -> {
                    Text(
                        text = viewState.errorMessage!!,
                        modifier = Modifier.semantics {
                            contentDescription = "Erro: ${viewState.errorMessage}"
                        },
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadProductDetail(productId) }) {
                        Text(stringResource(id = R.string.retry))
                    }
                }

                viewState.productDetail != null -> {
                    ProductDetailContent(product = viewState.productDetail!!)
                }
            }
        }
    }
}

/**
 * Main content of the product details screen.
 *
 * Why separate the content into a separate Composable?
 * - Improves code readability and organization.
 * - Eases reusability and testing of specific parts of the UI.
 * - Allows `ProductDetailScreen` to focus on state and navigation logic.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailContent(product: ProductDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Product title
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .semantics { contentDescription = "Product title: ${product.title}" }
        )

        // Image gallery (HorizontalPager for sliding between images)
        val pagerState = rememberPagerState(pageCount = { product.pictures.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .semantics { contentDescription = "Product image gallery" }
        ) { page ->

            GlideImage(
                model = product.pictures[page].url,
                contentDescription = "Image ${page + 1} of ${product.pictures.size} for product ${product.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Price
        Text(
            text = formatPrice(product.price, product.currencyId),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .semantics {
                    contentDescription =
                        "Price of product: ${
                            formatPrice(
                                product.price,
                                product.currencyId
                            )
                        }"
                }
        )

        // Condition and Quantity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Condition: ${
                    product.condition.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                    }
                }",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics {
                    contentDescription = "Product condition: ${product.condition}"
                }
            )
            Text(
                text = "Available: ${product.availableQuantity}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics {
                    contentDescription = "Available quantity: ${product.availableQuantity}"
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Free Shipping
        if (product.shipping?.freeShipping == true) {
            Text(
                text = stringResource(id = R.string.free_shipping),
                style = MaterialTheme.typography.bodyLarge,
                color = GreenCustom,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Sold Quantity
        Text(
            text = "Sold: ${product.soldQuantity}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .semantics {
                    contentDescription = "Sold quantity: ${product.soldQuantity}"
                }
        )

        // Warranty (if available)
        product.warranty?.let {
            Text(
                text = "Warranty: $it",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .semantics {
                        contentDescription = "Product warranty: $it"
                    }
            )
        }

        // Attributes (if available)
        if (!product.attributes.isNullOrEmpty()) {
            Text(
                text = "Features:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .semantics {
                        contentDescription = "Product features"
                    }
            )
            Column {
                product.attributes.forEach { attribute ->
                    AttributeItem(attribute = attribute)
                }
            }
        }
    }
}

/**
 * Component to display a product attribute.
 */
@Composable
fun AttributeItem(attribute: Attribute) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "${attribute.name}: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = attribute.valueName ?: "N/A",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    MyMercadoLivreApplicationTheme {
        ProductDetailScreen(navController = rememberNavController(), productId = "MLA123")
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailContentPreview() {
    MyMercadoLivreApplicationTheme {
        ProductDetailContent(
            product = ProductDetail(
                id = "MLA123",
                title = "Smart TV LED 50\" 4K Samsung",
                price = 2500.00,
                currencyId = "BRL",
                availableQuantity = 5,
                condition = "new",
                pictures = listOf(
                    Picture(
                        id = "1",
                        url = "https://http2.mlstatic.com/D_NQ_NP_2X_686906-MLA54967397940_042023-F.webp",
                        secureUrl = "",
                        size = "",
                        maxSize = ""
                    ),
                    Picture(
                        id = "2",
                        url = "https://http2.mlstatic.com/D_NQ_NP_2X_686906-MLA54967397940_042023-F.webp",
                        secureUrl = "",
                        size = "",
                        maxSize = ""
                    )
                ),
                shipping = Shipping(freeShipping = true),
                sellerId = 123456789,
                categoryId = "MLA1000",
                attributes = listOf(
                    Attribute(id = "BRAND", name = "Marca", valueName = "Samsung"),
                    Attribute(id = "MODEL", name = "Modelo", valueName = "AU8000"),
                    Attribute(
                        id = "SCREEN_SIZE",
                        name = "Tamanho da Tela",
                        valueName = "50 polegadas"
                    )
                ),
                warranty = "1 ano de garantia do fabricante",
                soldQuantity = 150
            )
        )
    }
}