package com.swago.seenthemlive.ui.screens.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object SearchRoute

fun NavController.navigateToSearch(navOptions: NavOptions? = null) =
    navigate(route = SearchRoute, navOptions)

fun NavGraphBuilder.searchScreen(
    onShowClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable<SearchRoute> {
        SearchRoute(
            onShowClick = onShowClick,
            onBackClick = onBackClick
        )
    }
}
