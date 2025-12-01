package com.swago.seenthemlive.ui.screens.map

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object MapRoute

fun NavController.navigateToMap(navOptions: NavOptions) =
    navigate(route = MapRoute, navOptions)

fun NavGraphBuilder.mapScreen() {
    composable<MapRoute> {
        MapRoute()
    }
}
