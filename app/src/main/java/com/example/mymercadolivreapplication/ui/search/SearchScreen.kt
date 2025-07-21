package com.example.mymercadolivreapplication.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import kotlinx.coroutines.delay

/**
 * Product search screen.
 * Allows the user to type a search term and initiate the search.
 * Displays loading, error, and results states.
 *
 * Why Jetpack Compose?
 * - Declarative approach: describes the UI as it should be, not how to build it.
 * - Less code: UI is more concise and easier to understand.
 * - Reactivity: the UI automatically updates when the state changes.
 * - Performance: optimized for efficient recomposition.
 * - Accessibility: built-in support for accessibility features.
 */
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    // State for the search term entered by the user
    var searchQuery by remember { mutableStateOf("") }

    // Collecting the UI state from the ViewModel
    val viewState by viewModel.viewState.collectAsState()

    val appName = stringResource(id = R.string.app_name)

    // Launching an effect when searchQuery changes
    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            // If the search field is empty, clear the results
            viewModel.clearSearch()
        } else {
            // Otherwise, trigger the search after 2 seconds debounce
            delay(2000L)
            viewModel.searchProducts(searchQuery)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = colorScheme.primary,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .semantics { contentDescription = appName }
        )

        // Text field for the search query
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
            },
            label = { Text(stringResource(id = R.string.search_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                // When the user presses 'Enter' on the keyboard, initiate the search
                if (searchQuery.isNotBlank()) {
                    navController.navigate("results/${searchQuery}")
                }
            }),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_icon) // Decorative icon, no description needed for screen readers
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search button
        Button(
            onClick = {
                if (searchQuery.isNotBlank()) {
                    navController.navigate("results/${searchQuery}")
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            val searchButtonDescription = stringResource(id = R.string.search_button)
            Text(
                stringResource(id = R.string.search_button),
                modifier = Modifier.semantics {
                    contentDescription = searchButtonDescription
                }
            )
        }

        // Display UI states
        when {
            viewState.isLoading -> {
                Spacer(modifier = Modifier.height(16.dp))

                val loadDescription = stringResource(id = R.string.loading_description)
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .semantics {
                            contentDescription = loadDescription
                        }
                )

                Text(
                    text = stringResource(id = R.string.loading),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { contentDescription = loadDescription }
                )
            }

            viewState.errorMessage != null -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = { viewModel.searchProducts(searchQuery) }
                ) {
                    Text(stringResource(id = R.string.retry))
                }
            }

            viewState.showNoResults -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // If results are available, navigation to the results screen will happen via NavController
            // Here we could display a preview of the results, but the requirement is to navigate to another screen
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MyMercadoLivreApplicationTheme {
        SearchScreen(navController = rememberNavController())
    }
}