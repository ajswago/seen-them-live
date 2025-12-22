package com.swago.seenthemlive.ui.screens.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable data class SearchResultRoute(val id: String)

fun NavController.navigateToSearchResult(showId: String, navOptions: NavOptions? = null) =
    navigate(route = SearchResultRoute(showId), navOptions)

fun NavGraphBuilder.searchResultScreen(
    onBackClick: () -> Unit
) {
    composable<SearchResultRoute> { entry ->
        val id = entry.toRoute<SearchResultRoute>().id
        SearchResultRoute(
            showId = id,
            onBackClick = onBackClick
        )
    }
}
