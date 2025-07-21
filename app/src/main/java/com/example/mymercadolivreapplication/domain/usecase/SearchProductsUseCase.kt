package com.example.mymercadolivreapplication.domain.usecase

import com.example.mymercadolivreapplication.data.model.Product
import com.example.mymercadolivreapplication.data.repository.MercadoLivreRepository
import com.example.mymercadolivreapplication.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use Case for searching products.
 * Encapsulates the business logic for product search, abstracting the data source.
 *
 * Why use a Use Case?
 * - Separates pure business logic from presentation logic (ViewModel).
 * - Makes business logic reusable and testable in isolation.
 * - Promotes the single responsibility principle.
 * - Facilitates code maintainability and scalability.
 */
class SearchProductsUseCase @Inject constructor(
    private val repository: MercadoLivreRepository
) {
    /**
     * Invokes the use case to search for products.
     *
     * @param query The search term.
     * @return A Flow of Resource containing a list of products.
     */
    operator fun invoke(query: String): Flow<Resource<List<Product>>> {
        // Example of business logic that could be added here:
        // - Validation of the query (e.g., minimum length)
        // - Filtering or transforming the results before passing them to the ViewModel
        // - Combining data from multiple sources (if there were more than one repository)

        return repository.searchProducts(query).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    // Here, we could apply some business rules to the products,
                    // such as filtering unavailable products or those with a price of zero.
                    // For now, we just pass through the results.
                    val products = resource.data?.results ?: emptyList()
                    Resource.Success(products)
                }

                is Resource.Error -> Resource.Error(resource.message!!)
                is Resource.Loading -> Resource.Loading()
            }
        }
    }
}