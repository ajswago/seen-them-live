package com.swago.seenthemlive.ui.screens.artists

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object ArtistListRoute

fun NavController.navigateToArtistList(navOptions: NavOptions) =
    navigate(route = ArtistListRoute, navOptions)

fun NavGraphBuilder.artistListScreen() {
    composable<ArtistListRoute> {
        ArtistListRoute()
    }
}
