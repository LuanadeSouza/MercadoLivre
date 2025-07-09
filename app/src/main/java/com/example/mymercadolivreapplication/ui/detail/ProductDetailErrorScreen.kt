package com.example.mymercadolivreapplication.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.ui.theme.DarkGray
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import com.example.mymercadolivreapplication.ui.theme.YellowCustom
import com.example.mymercadolivreapplication.utils.FirebaseAnalyticsManager

/**
 * Error screen for the Product Detail page.
 * Shown when the product detail could not be loaded.
 *
 * Provides options for the user to return to the previous search or navigate to the home screen.
 * Logs user interactions via Firebase Analytics.
 *
 * @param onBackToSearch Callback when the user wants to go back to the previous screen.
 * @param onGoToHome Callback when the user wants to go to the home screen.
 */
@Composable
fun ProductDetailErrorScreen(
    onBackToSearch: () -> Unit,
    onGoToHome: () -> Unit
) {

    val context = LocalContext.current
    val analyticsManager = FirebaseAnalyticsManager(context)

    val productDetailErrorMessage = stringResource(id = R.string.product_detail_error_message)
    val productDetailErrorSuggestion = stringResource(id = R.string.product_detail_error_suggestion)
    val backToResultSearch = stringResource(id = R.string.back_to_result_search)
    val goHomeScreen = stringResource(id = R.string.go_home_screen)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_not_found),
            contentDescription = productDetailErrorMessage,
            modifier = Modifier.semantics { contentDescription = productDetailErrorMessage }
        )

        Text(
            text = productDetailErrorMessage,
            style = Typography.titleSmall,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
                .semantics { contentDescription = productDetailErrorMessage }
        )

        Text(
            text = productDetailErrorSuggestion,
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .semantics { contentDescription = productDetailErrorSuggestion },
            textAlign = TextAlign.Center
        )

        Button(
            onClick = {
                analyticsManager.logEvent("click_back_to_search_button")
                onBackToSearch()
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = backToResultSearch },
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowCustom,
                contentColor = DarkGray
            )
        ) {
            Text(backToResultSearch)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                analyticsManager.logEvent("click_go_home_button")
                onGoToHome()
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = goHomeScreen },
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowCustom,
                contentColor = DarkGray
            )
        ) {
            Text(goHomeScreen)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailErrorScreenPreview() {
    MyMercadoLivreApplicationTheme {
        ProductDetailErrorScreen(
            onBackToSearch = {},
            onGoToHome = {}
        )
    }
}