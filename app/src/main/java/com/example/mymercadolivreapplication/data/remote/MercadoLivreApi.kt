package com.example.mymercadolivreapplication.data.remote

import com.example.mymercadolivreapplication.data.model.ProductDetail
import com.example.mymercadolivreapplication.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface defining the endpoints for the Mercado Libre API.
 *
 * Why use Retrofit?
 * - Industry-standard library for consuming REST APIs in Android.
 * - Native integration with Gson for JSON serialization/deserialization.
 * - Support for coroutines for asynchronous operations.
 * - Interceptors for logging and error handling.
 * - Type-safe: compile-time errors instead of runtime errors.
 */
interface MercadoLivreApi {

    /**
     * Searches for products on the Mercado Libre API.
     *
     * @param siteId The site ID (e.g., MLB for Brazil).
     * @param query The search term.
     * @param limit The maximum number of results (default: 50).
     * @param offset The offset for pagination (default: 0).
     * @return A Response containing the list of found products.
     *
     * Why use suspend?
     * - Allows the function to be called asynchronously without blocking the main thread.
     * - Native integration with coroutines for efficient asynchronous programming.
     * - Better performance and user experience.
     */
    @GET("sites/{site_id}/search")
    suspend fun searchProducts(
        @Path("site_id") siteId: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<SearchResponse>

    /**
     * Fetches the details of a specific product.
     *
     * @param itemId The product ID.
     * @return A Response containing the complete product details.
     */
    @GET("items/{item_id}")
    suspend fun getProductDetail(
        @Path("item_id") itemId: String
    ): Response<ProductDetail>

    companion object {
        const val BASE_URL = "https://api.mercadolibre.com/"
        const val SITE_ID_BRAZIL = "MLB"
    }
}