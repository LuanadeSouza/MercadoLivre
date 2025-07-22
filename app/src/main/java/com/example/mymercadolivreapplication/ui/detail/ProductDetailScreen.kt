package com.example.mymercadolivreapplication.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.mymercadolivreapplication.ui.detail.componet.ProductDetailContent
import com.example.mymercadolivreapplication.ui.theme.DarkGray
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import com.example.mymercadolivreapplication.ui.theme.YellowCustom

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
                        style = Typography.bodyLarge,
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
                    ProductDetailErrorScreen(
                        onBackToSearch = { navController.popBackStack() },
                        onGoToHome = { navController.navigate("search") { popUpTo(0) } }
                    )
                }

                viewState.productDetail != null -> {
                    ProductDetailContent(product = viewState.productDetail!!)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    MyMercadoLivreApplicationTheme {
        ProductDetailScreen(navController = rememberNavController(), productId = "MLA123")
    }
}
