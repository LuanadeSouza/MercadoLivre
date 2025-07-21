package com.example.mymercadolivreapplication.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymercadolivreapplication.data.model.SearchResponse
import com.example.mymercadolivreapplication.utils.AssetUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * ViewModel for handling the search functionality of the application.
 *
 * Why use a ViewModel?
 * - Encapsulates the business logic for product search, separating it from the UI layer.
 * - Uses a `MutableStateFlow` to manage the UI state, which allows for reactive updates to the UI.
 * - Cancels ongoing searches when a new search is initiated, ensuring the UI stays in sync.
 * - Provides methods for logging search events and handling errors during the search process.
 * - Simplifies managing search-related states such as loading, success, error, and empty results.
 *
 * @param context The application context, used to load mock data from the assets folder.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    // MutableStateFlow for the UI state, using SearchViewState
    private val _viewState = MutableStateFlow(SearchViewState())
    val viewState: StateFlow<SearchViewState> = _viewState.asStateFlow()

    private var searchJob: Job? = null

    /**
     * Initiates a product search based on the given query.
     * Cancels any ongoing search if a new query is provided.
     *
     * @param query The search term entered by the user.
     */
    fun searchProducts(query: String) {
        searchJob?.cancel() // Cancel the previous search if there is one

        if (query.isBlank()) {
            _viewState.value =
                SearchViewState(showNoResults = false) // Resets the state if the query is blank
            return
        }

        searchJob = viewModelScope.launch {
            // Start loading state and simulate repository call
            _viewState.value = SearchViewState(isLoading = true)

            try {
                // Try fetching the product by the query name
                val fileName = productMap[query.lowercase()]

                if (fileName != null) {
                    // Load the JSON from the assets folder
                    val json = AssetUtils.loadJsonFromAssets(context, "$fileName.json")

                    // Convert the loaded JSON to the data model
                    val type = object : TypeToken<SearchResponse>() {}.type
                    val searchResponse: SearchResponse = Gson().fromJson(json, type)

                    val products = searchResponse.results.orEmpty() // Get the mocked products

                    // Update UI state with the results
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        products = products,
                        showNoResults = products.isEmpty(),
                        errorMessage = null
                    )
                } else {
                    // If no products are found for the search query
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        errorMessage = "Produto não encontrado",
                        showNoResults = true
                    )
                }
            } catch (e: IOException) {
                // If an error occurs (e.g., file not found or loading error)
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    products = emptyList(),
                    errorMessage = "Erro ao carregar os resultados da pesquisa",
                    showNoResults = false
                )
            }
        }
    }

    /**
     * Clears the current search state.
     */
    fun clearSearch() {
        _viewState.value = SearchViewState()
    }

    // A map for associating query terms with their corresponding JSON files
    private val productMap = mapOf(
        "arroz" to "search-MLA-arroz",
        "cafe" to "search-MLA-cafe",
        "camisa" to "search-MLA-camisa",
        "zapatillas" to "search-MLA-zapatillas",
    )
}