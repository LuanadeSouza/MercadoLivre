package com.example.mymercadolivreapplication.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mymercadolivreapplication.ui.theme.BlueCustom

/**
 * Composable function to display rating stars based on available quantity.
 * It displays a filled star or an outlined star depending on the number of available items.
 *
 * @param availableQuantity The available quantity of the product, used to determine how many stars should be filled.
 */
@Composable
fun RatingStars(
    availableQuantity: Int,
) {
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
