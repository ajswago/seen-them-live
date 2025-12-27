package com.swago.seenthemlive.ui.screens.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object AboutRoute

fun NavController.navigateToAbout(navOptions: NavOptions? = null) =
    navigate(route = AboutRoute, navOptions)

fun NavGraphBuilder.aboutScreen(
    onBackClick: () -> Unit,
) {
    composable<AboutRoute> {
        AboutRoute(
            onBackClick = onBackClick,
        )
    }
}
