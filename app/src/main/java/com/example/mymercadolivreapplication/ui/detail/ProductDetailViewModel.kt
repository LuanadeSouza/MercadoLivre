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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the product details screen.
 * Manages the UI state and interacts with the repository to fetch product details.
 *
 * Follows the same principles as the SearchViewModel, but focuses on the details of a specific product.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager
) : ViewModel() {

    // UI state for product details using ProductDetailViewState
    private val _viewState = MutableStateFlow(ProductDetailViewState())
    val viewState: StateFlow<ProductDetailViewState> = _viewState.asStateFlow()

    /**
     * Loads the details of a specific product.
     *
     * @param productId The ID of the product to be loaded.
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
     * Clears the product detail state.
     * Useful when navigating to a new product or leaving the screen.
     */
    fun clearProductDetail() {
        _viewState.value = ProductDetailViewState()
    }
}