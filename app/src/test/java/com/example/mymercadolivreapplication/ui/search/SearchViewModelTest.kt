package com.example.mymercadolivreapplication.ui.search

import android.content.Context
import com.example.mymercadolivreapplication.util.MainDispatcherRule
import com.example.mymercadolivreapplication.utils.AssetUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


/**
 * Unit tests for the [SearchViewModel].
 *
 * This class tests the behavior of [SearchViewModel] under different search conditions
 * and validates the ViewModel's state management using mocked asset data.
 *
 * Covered test cases:
 * 1. Successfully loading products from a valid JSON file.
 * 2. Handling queries that are not mapped to any JSON file.
 * 3. Emitting an error when loading fails.
 * 4. Resetting the UI state when the search query is blank.
 *
 * Dependencies such as [AssetUtils] are mocked using MockK for isolation.
 * Coroutine flow emissions are handled using [MainDispatcherRule] and [app.cash.turbine].
 */
@ExperimentalCoroutinesApi
class SearchViewModelTest {

    /** Replaces Dispatchers.Main with a test dispatcher. */
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: SearchViewModel
    private val context = mockk<Context>(relaxed = true)

    /** Sets up the ViewModel and mocks before each test. */
    @Before
    fun setup() {
        mockkObject(AssetUtils)
        viewModel = SearchViewModel(context)
    }

    /**
     * Verifies that the ViewModel correctly loads and emits product list from a mapped JSON file.
     */
    @Test
    fun `should load products from mapped json file`() = runTest {
        val query = "arroz"
        val fileName = "search-MLA-arroz.json"

        val fakeJson = """
            {
              "results": [
                { "id": "MLA1", "title": "Arroz Tio João", "price": 15.0 }
              ]
            }
        """.trimIndent()

        every { AssetUtils.loadJsonFromAssets(any(), fileName) } returns fakeJson

        viewModel.searchProducts(query)

        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.products.size)
        assertEquals("Arroz Tio João", state.products.first().title)
        assertFalse(state.showNoResults)
        assertNull(state.errorMessage)
    }

    /**
     * Verifies that ViewModel emits no results state when the query is not mapped.
     */
    @Test
    fun `should emit no results when query is not mapped`() = runTest {
        val query = "inexistente"

        viewModel.searchProducts(query)

        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertFalse(state.isLoading)
        assertTrue(state.products.isEmpty())
        assertTrue(state.showNoResults)
        assertNull(state.errorMessage)
    }

    /**
     * Verifies that an error is emitted when asset loading fails.
     */
    @Test
    fun `should emit error when loading json fails`() = runTest {
        val query = "cafe"
        val fileName = "search-MLA-cafe.json"

        every {
            AssetUtils.loadJsonFromAssets(any(), fileName)
        } throws IOException("File not found")

        viewModel.searchProducts(query)

        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertFalse(state.isLoading)
        assertTrue(state.products.isEmpty())
        assertEquals("Erro ao carregar os resultados da pesquisa", state.errorMessage)
    }

    /**
     * Verifies that the state is reset when an empty query is submitted.
     */
    @Test
    fun `should reset state when query is blank`() {
        viewModel.searchProducts("")

        val state = viewModel.viewState.value
        assertTrue(state.products.isEmpty())
        assertFalse(state.showNoResults)
    }
}
