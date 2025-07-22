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
        // Coluna da esquerda com título
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
                .semantics { contentDescription = "magens do produto" }
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
            style = Typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .semantics {
                    contentDescription =
                        "Preço do produto: ${
                            formatPrice(product.price, product.currencyId)
                        }"
                }
        )

        // Condição
        val conditionFormatted = product.condition.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }

        LabeledText(
            label = "Condição",
            value = conditionFormatted,
            contentDescription = "Condição do produto: ${product.condition}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Frete grátis
        if (product.shipping?.freeShipping == true) {
            Text(
                text = stringResource(id = R.string.free_shipping),
                style = Typography.bodyLarge,
                color = GreenCustom,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quantidade vendida
        LabeledText(
            label = "Vendido",
            value = product.soldQuantity.toString(),
            contentDescription = "Quantidade vendida: ${product.soldQuantity}",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Garantia
        product.warranty?.let {
            LabeledText(
                label = "Garantia",
                value = it,
                contentDescription = "Garantia do produto: $it",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Atributos
        if (!product.attributes.isNullOrEmpty()) {
            Text(
                text = "Características:",
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .semantics {
                        contentDescription = "Características do produto"
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

