package com.example.mymercadolivreapplication.ui.search

import com.example.mymercadolivreapplication.data.model.Product

/**
 * Data class representing the UI state of the search screen.
 *
 * Why use a ViewState?
 * - Encapsulates all possible UI states in a single object.
 * - Makes it easier to observe state changes in the UI.
 * - Ensures the UI is rendered consistently with the current state.
 * - Helps avoid invalid or inconsistent UI states.
 * - Makes the code more readable and easier to understand.
 *
 * @param isLoading Indicates if the search is in progress.
 * @param products List of products found.
 * @param errorMessage Error message to be displayed, if any.
 * @param showNoResults Indicates if the "no results" message should be shown.
 */
data class SearchViewState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
    val showNoResults: Boolean = false
)