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
 * Composable function to display a product item.
 * It includes the product image, title, price, original price (if available),
 * available quantity, and a rating based on the available quantity.
 *
 * Why Card?
 * - Provides an elevated surface that groups related content, enhancing the visual experience.
 * - Allows for a clickable interaction, enabling navigation to product details.
 *
 * Why GlideImage?
 * - Optimized for loading and displaying images in Android.
 * - Manages caching, resizing, and placeholders automatically for smooth performance.
 *
 * Accessibility:
 * - Uses `contentDescription` for product image, ensuring accessibility for screen readers.
 * - Price formatting is handled by `NumberFormat`, ensuring proper localization for different currencies.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp) // Set card width
            .height(270.dp) // Set card height
            .clickable(onClick = onClick) // Make the card clickable to navigate to product details
            .semantics {
                contentDescription = "Produto: ${product.title}, Preço: ${
                    formatPrice(
                        product.price,
                        product.currencyId
                    )
                }" // Adding semantic content description for accessibility
            },
        colors = CardDefaults.cardColors(containerColor = Color.White) // Set card background color
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp), // Add padding around the column
            verticalArrangement = Arrangement.spacedBy(8.dp), // Add spacing between the items
            horizontalAlignment = Alignment.CenterHorizontally // Align content horizontally in the center
        ) {

            GlideImage(
                model = product.thumbnail, // Set image model
                contentDescription = stringResource(id = R.string.product_details) + ": ${product.title}", // Set content description for accessibility
                modifier = Modifier.size(140.dp), // Set image size
                contentScale = ContentScale.Fit // Ensure the image fits the view without distortion
            )

            Column(
                modifier = Modifier.weight(1f) // Make the column take the available space
            ) {

                Text(
                    text = product.title, // Display product title
                    style = Typography.labelSmall,
                    maxLines = 2, // Limit the title to two lines
                    modifier = Modifier.semantics {
                        contentDescription = "Título do produto: ${product.title}"
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically, // Align items vertically in the center
                    horizontalArrangement = Arrangement.spacedBy(4.dp) // Space between the stars and the quantity
                ) {
                    RatingStars(availableQuantity = product.availableQuantity) // Display rating stars
                    Text(
                        text = "(${product.availableQuantity})", // Display the available quantity
                        style = Typography.displaySmall,
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Avaliações: ${product.availableQuantity}"
                            }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp)) // Add spacing before displaying the prices

                // If original price exists, display it with a strikethrough decoration
                product.originalPrice?.let {
                    Text(
                        text = formatPrice(product.originalPrice, product.currencyId), // Format and display original price
                        style = Typography.labelSmall,
                        fontWeight = FontWeight.Normal, // Set normal font weight
                        textDecoration = TextDecoration.LineThrough, // Apply strikethrough decoration for original price
                        modifier = Modifier.semantics {
                            contentDescription =
                                "Preço original: ${formatPrice(product.price, product.currencyId)}"
                        }
                    )
                }

                // Display promotional price and discount percentage
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp) // Add space between the price and discount
                ) {
                    // Display promotional price
                    Text(
                        text = formatPrice(product.price, product.currencyId),
                        style = Typography.labelMedium,
                        fontWeight = FontWeight.Bold, // Make the price bold
                        modifier = Modifier.semantics {
                            contentDescription =
                                "Preço promocional: ${formatPrice(product.price, product.currencyId)}"
                        }
                    )

                    // If original price exists, calculate and display the discount percentage
                    product.originalPrice?.let { originalPrice ->
                        val discountPercentage =
                            ((originalPrice - product.price) / originalPrice) * 100 // Calculate the discount percentage

                        Text(
                            text = "${discountPercentage.roundToInt()}% OFF", // Display the discount percentage
                            style = Typography.labelSmall,
                            color = GreenCustom, // Set the text color to green for the discount
                            modifier = Modifier.semantics {
                                contentDescription =
                                    "Desconto de campanha: ${discountPercentage.roundToInt()}%"
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp)) // Add spacing before the shipping info

                // Display "Free Shipping" text if applicable
                if (product.shipping?.freeShipping == true) {
                    Text(
                        text = stringResource(id = R.string.free_shipping),
                        style = Typography.labelMedium,
                        color = GreenCustom, // Set the text color to green for free shipping
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
 * Utility function to format the price according to the currency and locale.
 * It ensures that the monetary value is formatted correctly based on the region and currency.
 *
 * Why use NumberFormat?
 * - Automatically handles currency symbols, thousand separators, and decimal points.
 * - Essential for correct localization and accessibility.
 */
fun formatPrice(price: Double, currencyId: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    format.currency = java.util.Currency.getInstance(currencyId)
    return format.format(price)
}

/**
 * Composable function to display rating stars based on available quantity.
 * It displays a filled star or an outlined star depending on the number of available items.
 *
 * @param availableQuantity The available quantity of the product, used to determine how many stars should be filled.
 */
@Composable
fun RatingStars(availableQuantity: Int) {
    val starsFilled = if (availableQuantity >= 10) {
        5
    } else {
        (availableQuantity / 2).coerceAtMost(5)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        for (i in 1..5) {
            if (i <= starsFilled) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Estrela $i",
                    tint = BlueCustom,
                    modifier = Modifier.size(8.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Estrela $i",
                    tint = Color.Gray,
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