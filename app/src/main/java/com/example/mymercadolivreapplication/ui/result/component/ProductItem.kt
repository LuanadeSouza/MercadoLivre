package com.example.mymercadolivreapplication.ui.result.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.data.model.Product
import com.example.mymercadolivreapplication.data.model.Shipping
import com.example.mymercadolivreapplication.ui.theme.GreenCustom
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import java.text.NumberFormat
import java.util.Locale


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
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = "Produto: ${product.title}, Preço: ${
                    formatPrice(
                        product.price,
                        product.currencyId
                    )
                }"
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
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

                // Exibindo o preço original riscado e o preço promocional sem risco
                Text(
                    text = formatPrice(product.price, product.currencyId),
                    style = Typography.labelSmall,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.LineThrough, // Riscando o preço original
                    modifier = Modifier.semantics {
                        contentDescription =
                            "Preço original: ${formatPrice(product.price, product.currencyId)}"
                    }
                )

                Text(
                    text = formatPrice(product.price, product.currencyId),
                    style = Typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.semantics {
                        contentDescription =
                            "Preço do promocional: ${
                                formatPrice(
                                    product.price,
                                    product.currencyId
                                )
                            }"
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

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

