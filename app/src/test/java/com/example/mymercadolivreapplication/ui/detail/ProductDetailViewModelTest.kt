package com.example.mymercadolivreapplication.ui.detail

import android.content.Context
import app.cash.turbine.test
import com.example.mymercadolivreapplication.util.MainDispatcherRule
import com.example.mymercadolivreapplication.utils.AssetUtils
import com.example.mymercadolivreapplication.utils.FirebaseAnalyticsManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


/**
 * Unit tests for the [ProductDetailViewModel].
 *
 * This class tests the behavior of [ProductDetailViewModel] when interacting with mock data
 * and simulates different loading scenarios from the assets folder.
 *
 * The following test cases are covered:
 * 1. Successful product detail loading from a valid JSON asset file.
 * 2. Handling of an IOException when the asset file is not found.
 * 3. Resetting the UI state using `clearProductDetail`.
 *
 * Dependencies such as [AssetUtils] and [FirebaseAnalyticsManager] are mocked
 * using MockK to isolate and verify the ViewModel behavior independently of the environment.
 *
 * The test environment uses a custom [MainDispatcherRule] to properly handle coroutines during unit tests.
 */
@ExperimentalCoroutinesApi
class ProductDetailViewModelTest {
    /**
     * A custom coroutine rule that overrides the Main dispatcher for unit testing with coroutines.
     */
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: ProductDetailViewModel
    private val context = mockk<Context>(relaxed = true)
    private val analyticsManager = mockk<FirebaseAnalyticsManager>(relaxed = true)

    /**
     * Sets up the required mocks and initializes the ViewModel before each test.
     */
    @Before
    fun setup() {
        mockkObject(AssetUtils)
        viewModel = ProductDetailViewModel(context, analyticsManager)
    }

    /**
     * Verifies that the ViewModel correctly loads and parses a product detail from a valid JSON string.
     */
    @Test
    fun `should load product detail successfully`() = runTest {
        val productId = "MLA123"
        val fileName = "item-$productId.json"

        val fakeJson = """
        {
          "id": "$productId",
          "title": "Test Product",
          "price": 100.0,
          "currency_id": "BRL",
          "category_id": "cat-01"
        }
    """.trimIndent()

        every { AssetUtils.loadJsonFromAssets(any(), fileName) } returns fakeJson

        viewModel.loadProductDetail(productId)

        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertFalse(state.isLoading)
        assertEquals("Test Product", state.productDetail?.title)
        assertNull(state.errorMessage)
    }

    /**
     * Verifies that an error message is emitted when the JSON file is not found or fails to load.
     */
    @Test
    fun `should emit error when file not found`() = runTest {
        val productId = "invalid"
        val fileName = "item-${productId.uppercase()}.json"

        every {
            AssetUtils.loadJsonFromAssets(any(), fileName)
        } throws IOException("File not found")

        viewModel.loadProductDetail(productId)

        advanceUntilIdle() // Aguarda todas as coroutines terminarem

        val state = viewModel.viewState.value

        assertFalse(state.isLoading)
        assertNull(state.productDetail)
        assertEquals("Erro ao carregar os detalhes do produto", state.errorMessage)
    }

    /**
     * Verifies that calling `clearProductDetail` resets the state to its default values.
     */
    @Test
    fun `should reset state on clearProductDetail`() {
        viewModel.clearProductDetail()

        val state = viewModel.viewState.value
        assertFalse(state.isLoading)
        assertNull(state.productDetail)
        assertNull(state.errorMessage)
    }
}