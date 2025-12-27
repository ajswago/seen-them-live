package com.swago.seenthemlive.ui.screens.playlist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object CreatePlaylistRoute

fun NavController.navigateToCreatePlaylist(navOptions: NavOptions? = null) =
    navigate(route = CreatePlaylistRoute, navOptions)

fun NavGraphBuilder.createPlaylistScreen(
    onBackClick: () -> Unit,
    onPlaylistConfirmed: () -> Unit,
) {
    composable<CreatePlaylistRoute> {
        CreatePlaylistRoute(
            onBackClick = onBackClick,
            onPlaylistConfirmed = onPlaylistConfirmed
        )
    }
}
