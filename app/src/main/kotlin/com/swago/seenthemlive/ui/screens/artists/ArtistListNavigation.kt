package com.swago.seenthemlive.ui.screens.artists

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.swago.seenthemlive.ui.components.ProfileMenuItem
import kotlinx.serialization.Serializable

@Serializable object ArtistListRoute

fun NavController.navigateToArtistList(navOptions: NavOptions) =
    navigate(route = ArtistListRoute, navOptions)

fun NavGraphBuilder.artistListScreen(
    onProfileMenuOption: (ProfileMenuItem) -> Unit,
    onArtistClick: (String) -> Unit,
) {
    composable<ArtistListRoute> {
        ArtistListRoute(
            onProfileMenuOption = onProfileMenuOption,
            onArtistClick = onArtistClick
        )
    }
}
