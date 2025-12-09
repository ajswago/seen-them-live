package com.swago.seenthemlive.ui.screens.shows

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable data class ShowRoute(val id: String)

fun NavController.navigateToShow(showId: String, navOptions: NavOptions? = null) =
    navigate(route = ShowRoute(showId), navOptions)

fun NavGraphBuilder.showScreen(
    onBackClick: () -> Unit
) {
    composable<ShowRoute> { entry ->
        val id = entry.toRoute<ShowRoute>().id
        ShowRoute(
            showId = id,
            onBackClick = onBackClick
        )
    }
}
