package com.example.mymercadolivreapplication.ui.result.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.mymercadolivreapplication.ui.theme.BlueCustom
import com.example.mymercadolivreapplication.ui.theme.GreenCustom
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt


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
            .height(270.dp)
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
                modifier = Modifier.size(140.dp),
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

                // Exibindo o campo availableQuantity
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RatingStars(availableQuantity = product.availableQuantity) // Estrelas
                    Text(
                        text = "(${product.availableQuantity})", // Quantidade de avaliações
                        style = Typography.displaySmall,
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Avaliações: ${product.availableQuantity}"
                            }
                    )
                }


                Spacer(modifier = Modifier.height(4.dp))

                product.originalPrice?.let {
                    Text(
                        text = formatPrice(product.originalPrice, product.currencyId),
                        style = Typography.labelSmall,
                        fontWeight = FontWeight.Normal,
                        textDecoration = TextDecoration.LineThrough, // Riscando o preço original
                        modifier = Modifier.semantics {
                            contentDescription =
                                "Preço original: ${formatPrice(product.price, product.currencyId)}"
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Exibe o preço promocional
                    Text(
                        text = formatPrice(product.price, product.currencyId),
                        style = Typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.semantics {
                            contentDescription =
                                "Preço promocional: ${
                                    formatPrice(
                                        product.price,
                                        product.currencyId
                                    )
                                }"
                        }
                    )

                    product.originalPrice?.let { originalPrice ->
                        val discountPercentage =
                            ((originalPrice - product.price) / originalPrice) * 100

                        Text(
                            text = "${discountPercentage.roundToInt()}% OFF",
                            style = Typography.labelSmall,
                            color = GreenCustom,
                            modifier = Modifier.semantics {
                                contentDescription =
                                    "Desconto de campanha: ${discountPercentage.roundToInt()}%"
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (product.shipping?.freeShipping == true) {
                    Text(
                        text = stringResource(id = R.string.free_shipping),
                        style = Typography.labelMedium,
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

@Composable
fun RatingStars(availableQuantity: Int) {
    val starsFilled = if (availableQuantity >= 10) {
        5 // Preenche 5 estrelas se o número de avaliações for 10 ou mais
    } else {
        (availableQuantity / 2).coerceAtMost(5) // Calcula estrelas proporcionais e garante que não ultrapasse 5
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Preenche as estrelas de acordo com a avaliação
        for (i in 1..5) {
            // Exibe estrela preenchida ou vazia, dependendo da avaliação
            if (i <= starsFilled) {
                Icon(
                    imageVector = Icons.Filled.Star, // Estrela preenchida
                    contentDescription = "Estrela $i",
                    tint = BlueCustom, // Cor da estrela preenchida
                    modifier = Modifier.size(8.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Star, // Estrela vazia
                    contentDescription = "Estrela $i",
                    tint = Color.Gray, // Cor da estrela vazia
                    modifier = Modifier.size(8.dp)
                )
            }
        }
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
                price = 5100.99,
                originalPrice = 5999.99,
                currencyId = "BRL",
                availableQuantity = 7,
                thumbnail = "@drawable/ic_launcher_background",
                condition = "new",
                shipping = Shipping(freeShipping = true),
                seller = null,
                attributes = null
            )
        ) {}
    }
}

