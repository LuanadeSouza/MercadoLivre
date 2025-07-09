package com.example.mymercadolivreapplication.ui.detail.componet

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.data.model.Attribute
import com.example.mymercadolivreapplication.data.model.Picture
import com.example.mymercadolivreapplication.data.model.ProductDetail
import com.example.mymercadolivreapplication.data.model.Shipping
import com.example.mymercadolivreapplication.ui.component.RatingStars
import com.example.mymercadolivreapplication.ui.result.component.formatPrice
import com.example.mymercadolivreapplication.ui.theme.GreenCustom
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Main content of the product details screen.
 *
 * Displays all relevant product information such as images, title, rating, pricing, condition,
 * shipping details, quantity sold, warranty and product attributes.
 *
 * @param product The [ProductDetail] object containing all product information.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailContent(product: ProductDetail) {

    val warrantyLabel = stringResource(id = R.string.warranty_label)
    val soldLabel = stringResource(id = R.string.sold_label)
    val productFeaturesLabel = stringResource(id = R.string.product_features_label)
    val productFeaturesDescription = stringResource(id = R.string.product_features_description)
    val conditionLabel = stringResource(id = R.string.condition_label)
    val freeShippingText = stringResource(id = R.string.free_shipping)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            // Rating alinhado à direita
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RatingStars(
                        availableQuantity = product.availableQuantity,
                    )
                    Text(
                        text = "(${product.availableQuantity})",
                        style = Typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = "Avaliações: ${product.availableQuantity}"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Título
            Text(
                text = product.title,
                style = Typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Título do produto: ${product.title}"
                    }
            )
        }


        // Image gallery (HorizontalPager for sliding between images)
        val pagerState = rememberPagerState(pageCount = { product.pictures.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .semantics { contentDescription = "imagens do produto" }
        ) { page ->

            GlideImage(
                model = product.pictures[page].url,
                contentDescription = "Image ${page + 1} of ${product.pictures.size} for product ${product.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            product.originalPrice?.let {
                Text(
                    text = formatPrice(it, product.currencyId),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.LineThrough,
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .semantics {
                            contentDescription =
                                "Preço original: ${formatPrice(it, product.currencyId)}"
                        }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = formatPrice(product.price, product.currencyId),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.semantics {
                        contentDescription =
                            "Preço atual: ${formatPrice(product.price, product.currencyId)}"
                    }
                )

                product.originalPrice?.let { original ->
                    val discountPercentage = ((original - product.price) / original) * 100
                    Text(
                        text = "${discountPercentage.roundToInt()}% OFF",
                        style = Typography.bodySmall,
                        color = GreenCustom,
                        modifier = Modifier.semantics {
                            contentDescription = "Desconto de ${discountPercentage.roundToInt()}%"
                        }
                    )
                }
            }
        }

        // Condition
        val conditionFormatted = product.condition.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }

        LabeledText(
            label = conditionLabel,
            value = conditionFormatted,
            contentDescription = "Condição do produto: ${product.condition}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Free Shipping
        if (product.shipping?.freeShipping == true) {
            Text(
                text = freeShippingText,
                style = Typography.bodyLarge,
                color = GreenCustom,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.semantics {
                    contentDescription = freeShippingText
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sold Quantity
        LabeledText(
            label = soldLabel,
            value = product.soldQuantity.toString(),
            contentDescription = "Quantidade vendida: ${product.soldQuantity}",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Warranty
        product.warranty?.let {
            LabeledText(
                label = warrantyLabel,
                value = it,
                contentDescription = "Garantia do produto: $it",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Atributes
        if (!product.attributes.isNullOrEmpty()) {
            Text(
                text = productFeaturesLabel,
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .semantics {
                        contentDescription = productFeaturesDescription
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

@Preview(showBackground = true)
@Composable
fun ProductDetailContentPreview() {
    MyMercadoLivreApplicationTheme {
        ProductDetailContent(
            product = ProductDetail(
                id = "MLA123",
                title = "Smart TV LED 50\" 4K Samsung",
                price = 2500.00,
                originalPrice = 2800.00,
                currencyId = "BRL",
                availableQuantity = 5,
                condition = "novo",
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

@Composable
fun LabeledText(
    label: String,
    value: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append("$label: ")
            }
            append(value)
        },
        style = Typography.bodyLarge,
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
        }
    )
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
        LabeledText(
            label = attribute.name,
            value = attribute.valueName ?: "N/A",
            contentDescription = "${attribute.name}: ${attribute.valueName ?: "N/A"}"
        )
    }
}

