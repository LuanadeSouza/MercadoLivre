package com.example.mymercadolivreapplication.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymercadolivreapplication.data.model.ProductDetail
import com.example.mymercadolivreapplication.utils.AssetUtils
import com.example.mymercadolivreapplication.utils.FirebaseAnalyticsManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the product detail screen.
 *
 * Core responsibilities:
 * - Load product details from the local assets folder.
 * - Handle loading, success, and error UI states.
 * - Log analytics events via Firebase.
 * - Reset state when navigating away.
 *
 * Usage:
 * This ViewModel is lifecycle-aware and scoped to the product detail screen.
 * It is initialized with the application context and the Firebase analytics manager
 * through Hilt dependency injection.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager
) : ViewModel() {

    /**
     * Backing property for the current UI state.
     * Exposes the state as an immutable [StateFlow] to observers.
     */
    private val _viewState = MutableStateFlow(ProductDetailViewState())
    val viewState: StateFlow<ProductDetailViewState> = _viewState.asStateFlow()

    /**
     * Backing property for the current UI state.
     * Exposes the state as an immutable [StateFlow] to observers.
     */
    fun loadProductDetail(productId: String) {
        viewModelScope.launch {
            // Creates the file name based on the productId, e.g., "item-MLA1234.json"
            val fileName = "item-${productId.uppercase(Locale.getDefault())}.json"

            try {
                // Load the JSON from the assets folder
                val json = AssetUtils.loadJsonFromAssets(context, fileName)

                // Convert the loaded JSON to the data model
                val type = object : TypeToken<ProductDetail>() {}.type
                val productDetail: ProductDetail = Gson().fromJson(json, type)

                // Update the UI state with product details
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    productDetail = productDetail,
                    errorMessage = null
                )

                // Log the product view event in Firebase (if necessary)
                firebaseAnalyticsManager.logViewItemEvent(
                    itemId = productDetail.id,
                    itemName = productDetail.title,
                    itemCategory = productDetail.categoryId,
                    price = productDetail.price,
                    currency = productDetail.currencyId
                )

            } catch (e: IOException) {
                // Handle error if the file is not found or an issue occurs while loading the JSON
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    productDetail = null,
                    errorMessage = "Erro ao carregar os detalhes do produto"
                )
            }
        }
    }

    /**
     * Resets the product detail UI state to its initial values.
     *
     * Useful when leaving the screen or navigating to a different product.
     */
    fun clearProductDetail() {
        _viewState.value = ProductDetailViewState()
    }
}