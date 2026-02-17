package com.swago.seenthemlive.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.swago.seenthemlive.ui.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable object HomeRoute

fun NavController.navigateToHomeNavHost(navOptions: NavOptions? = null) {
    navigate(route = HomeRoute, navOptions)
}

fun NavGraphBuilder.homeNavHost(
    logout: () -> Unit
) {
    composable<HomeRoute> {
        HomeRoute(logout = logout)
    }
}
