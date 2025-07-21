package com.example.mymercadolivreapplication.ui.detail

import com.example.mymercadolivreapplication.data.model.ProductDetail

/**
 * Data class representing the UI state of the product details screen.
 *
 * @param isLoading Indicates if the product details are being loaded.
 * @param productDetail The full details of the product.
 * @param errorMessage The error message to be displayed, if any.
 */
data class ProductDetailViewState(
    val isLoading: Boolean = false,
    val productDetail: ProductDetail? = null,
    val errorMessage: String? = null
)