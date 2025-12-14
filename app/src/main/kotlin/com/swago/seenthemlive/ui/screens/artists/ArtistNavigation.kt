package com.swago.seenthemlive.ui.screens.artists

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable data class ArtistRoute(val id: String)

fun NavController.navigateToArtist(artistId: String, navOptions: NavOptions? = null) =
    navigate(route = ArtistRoute(artistId), navOptions)

fun NavGraphBuilder.artistScreen(
    onBackClick: () -> Unit
) {
    composable<ArtistRoute> { entry ->
        val id = entry.toRoute<ArtistRoute>().id
        ArtistRoute(
            artistId = id,
            onBackClick = onBackClick
        )
    }
}
