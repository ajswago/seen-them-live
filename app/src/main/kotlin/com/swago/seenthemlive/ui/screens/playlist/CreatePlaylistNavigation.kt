package com.swago.seenthemlive.ui.screens.playlist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.spotify.sdk.android.auth.AuthorizationRequest
import kotlinx.serialization.Serializable

@Serializable object CreatePlaylistRoute

fun NavController.navigateToCreatePlaylist(navOptions: NavOptions? = null) =
    navigate(route = CreatePlaylistRoute, navOptions)

fun NavGraphBuilder.createPlaylistScreen(
    onBackClick: () -> Unit,
    onSpotifyAuth: (AuthorizationRequest, (String) -> Unit) -> Unit,
    onPlaylistConfirmed: () -> Unit,
) {
    composable<CreatePlaylistRoute> {
        CreatePlaylistRoute(
            onBackClick = onBackClick,
            onSpotifyAuth = onSpotifyAuth,
            onPlaylistConfirmed = onPlaylistConfirmed
        )
    }
}
