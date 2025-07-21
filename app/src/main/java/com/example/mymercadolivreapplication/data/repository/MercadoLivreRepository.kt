package com.example.mymercadolivreapplication.data.repository

import android.content.Context
import com.example.mymercadolivreapplication.data.model.ProductDetail
import com.example.mymercadolivreapplication.data.model.SearchResponse
import com.example.mymercadolivreapplication.data.remote.MercadoLivreApi
import com.example.mymercadolivreapplication.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Repository responsible for managing access to the Mercado Libre API data.
 *
 * Why use the Repository pattern?
 * - Abstracts the data source (API, local database, cache)
 * - Centralizes data access logic
 * - Facilitates unit testing (can be easily mocked)
 * - Allows data source changes without affecting the UI
 * - Implements the single responsibility principle
 *
 * Why use @Singleton?
 * - Ensures a single instance of the repository across the application
 * - Prevents unnecessary object creation
 * - Enables cache sharing across different parts of the app
 */
@Singleton
class MercadoLivreRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: MercadoLivreApi
) {

    /**
     * Loads a JSON file from the assets folder.
     *
     * @param context The application context
     * @param fileName The name of the JSON file
     * @return The JSON content as a String, or null if an error occurs
     */

    private fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Searches for products using the Mercado Libre API.
     *
     * Why use Flow?
     * - Allows emission of multiple values over time (Loading -> Success/Error)
     * - Natively integrates with Jetpack Compose for automatic recomposition
     * - Supports reactive operators (map, filter, etc.)
     * - Automatically cancels when no observers are present
     *
     * @param query Search term
     * @return A Flow that emits Resource<SearchResponse> representing the result of the search
     */

    fun searchProducts(query: String): Flow<Resource<SearchResponse>> = flow {
        try {
            emit(Resource.Loading())

            Timber.d("Searching products with query: $query")

            val response = api.searchProducts(
                siteId = MercadoLivreApi.SITE_ID_BRAZIL,
                query = query
            )

            if (response.isSuccessful) {
                response.body()?.let { searchResponse ->
                    Timber.d("Search successful. Found ${searchResponse.results.size} products")
                    emit(Resource.Success(searchResponse))
                } ?: run {
                    Timber.e("Search response body is null")
                    emit(Resource.Error("Resposta vazia do servidor"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    404 -> "Nenhum resultado encontrado"
                    500 -> "Erro interno do servidor"
                    else -> "Erro na busca: ${response.code()}"
                }
                Timber.e("Search failed with code: ${response.code()}")
                emit(Resource.Error(errorMessage))
            }

        } catch (e: HttpException) {
            // Erro HTTP (4xx, 5xx)
            val errorMessage = when (e.code()) {
                404 -> "Nenhum resultado encontrado"
                500 -> "Erro interno do servidor"
                else -> "Erro de conexão: ${e.code()}"
            }
            Timber.e(e, "HTTP error during search")
            emit(Resource.Error(errorMessage))

        } catch (e: SocketTimeoutException) {
            Timber.e(e, "Timeout error during search")
            emit(Resource.Error("A requisição demorou muito para ser processada. Tente novamente mais tarde."))

        } catch (e: UnknownHostException) {
            Timber.e(e, "No internet connection")
            emit(Resource.Error("Sem conexão com a internet. Verifique sua conexão e tente novamente."))

        } catch (e: IOException) {
            Timber.e(e, "Network error during search")
            emit(Resource.Error("Erro de conexão. Verifique sua internet e tente novamente."))

        } catch (e: Exception) {
            Timber.e(e, "Unexpected error during search")
            emit(Resource.Error("Ocorreu um erro inesperado. Tente novamente mais tarde."))
        }
    }

    /**
     * Fetches the details of a specific product.
     *
     * @param productId The product ID
     * @return A Flow that emits Resource<ProductDetail> representing the product details
     */
    fun getProductDetail(productId: String): Flow<Resource<ProductDetail>> = flow {
        try {
            emit(Resource.Loading())

            Timber.d("Getting product detail for ID: $productId")

            val response = api.getProductDetail(productId)

            if (response.isSuccessful) {
                response.body()?.let { productDetail ->
                    Timber.d("Product detail loaded successfully")
                    emit(Resource.Success(productDetail))
                } ?: run {
                    Timber.e("Product detail response body is null")
                    emit(Resource.Error("Produto não encontrado"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    404 -> "Produto não encontrado"
                    500 -> "Erro interno do servidor"
                    else -> "Erro ao carregar produto: ${response.code()}"
                }
                Timber.e("Product detail failed with code: ${response.code()}")
                emit(Resource.Error(errorMessage))
            }

        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                404 -> "Produto não encontrado"
                500 -> "Erro interno do servidor"
                else -> "Erro de conexão: ${e.code()}"
            }
            Timber.e(e, "HTTP error during product detail fetch")
            emit(Resource.Error(errorMessage))

        } catch (e: IOException) {
            Timber.e(e, "Network error during product detail fetch")
            emit(Resource.Error("Erro de conexão. Verifique sua internet."))

        } catch (e: Exception) {
            Timber.e(e, "Unexpected error during product detail fetch")
            emit(Resource.Error("Ocorreu um erro inesperado"))
        }
    }
}