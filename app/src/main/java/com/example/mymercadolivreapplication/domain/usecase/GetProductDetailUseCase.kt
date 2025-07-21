package com.example.mymercadolivreapplication.domain.usecase

import com.example.mymercadolivreapplication.data.model.ProductDetail
import com.example.mymercadolivreapplication.data.repository.MercadoLivreRepository
import com.example.mymercadolivreapplication.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case to retrieve the details of a specific product.
 * Encapsulates the business logic for fetching product details.
 */
class GetProductDetailUseCase @Inject constructor(
    private val repository: MercadoLivreRepository
) {
    /**
     * Invokes the use case to get the details of a product.
     *
     * @param productId The product ID.
     * @return A Flow of Resource containing the product details.
     */
    operator fun invoke(productId: String): Flow<Resource<ProductDetail>> {
        // Here we could add specific validations or transformations
        // for product details, such as:
        // - Validation of the product ID format
        // - Enrichment of the data with information from other sources
        // - Application of specific business rules

        return repository.getProductDetail(productId)
    }
}