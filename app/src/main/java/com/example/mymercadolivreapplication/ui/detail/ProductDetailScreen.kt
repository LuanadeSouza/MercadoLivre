package com.example.mymercadolivreapplication.ui.detail

import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.mymercadolivreapplication.utils.FirebaseAnalyticsManager
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Product details screen.
 * Displays detailed information about a specific product.
 *
 * Responsibilities:
 * - Loads and displays product detail based on a given productId.
 * - Handles different UI states: loading, error, and content.
 * - Provides navigation back and home actions.
 * - Logs screen view and user interactions to Firebase Analytics.
 *
 * @param navController Controller to handle back and navigation actions.
 * @param productId Identifier for the product to load details for.
 * @param analyticsManager Manager to log analytics events (injected by default).
 * @param viewModel ViewModel responsible for fetching product details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    analyticsManager: FirebaseAnalyticsManager = FirebaseAnalyticsManager(
        context = LocalContext.current
    ),
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    // Collecting the UI state from the ViewModel
    val viewState by viewModel.viewState.collectAsState()

    // Trigger product load and screen view logging on composition
    LaunchedEffect(productId) {
        viewModel.loadProductDetail(productId)

        // Log screen view event
        analyticsManager.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "product_detail_screen")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ProductDetailScreen")
            }
        )
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
                            analyticsManager.logEvent("click_back_button")
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
                        onBackToSearch = {
                            analyticsManager.logEvent("click_back_to_search_button")
                            navController.popBackStack()
                        },
                        onGoToHome = {
                            analyticsManager.logEvent("click_go_home_button")
                            navController.navigate("search") { popUpTo(0) }
                        }
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
