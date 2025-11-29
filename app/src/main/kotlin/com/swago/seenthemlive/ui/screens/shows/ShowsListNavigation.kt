package com.swago.seenthemlive.ui.screens.shows

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object ShowsListRoute

fun NavController.navigateToShowsList(navOptions: NavOptions) =
    navigate(route = ShowsListRoute, navOptions)

fun NavGraphBuilder.showsListScreen() {
    composable<ShowsListRoute> {
        ShowsListRoute()
    }
}
