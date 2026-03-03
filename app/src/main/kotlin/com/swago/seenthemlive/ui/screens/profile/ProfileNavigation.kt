package com.swago.seenthemlive.ui.screens.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) =
    navigate(route = ProfileRoute, navOptions)

fun NavGraphBuilder.profileScreen(
    onBackClick: () -> Unit,
    onArtistClick: (String) -> Unit,
) {
    composable<ProfileRoute> {
        ProfileRoute(
            onBackClick = onBackClick,
            onArtistClick = onArtistClick
        )
    }
}
