package com.swago.seenthemlive.ui.screens.shows

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.swago.seenthemlive.ui.components.ProfileMenuItem
import kotlinx.serialization.Serializable

@Serializable object ShowsListRoute

fun NavController.navigateToShowsList(navOptions: NavOptions) =
    navigate(route = ShowsListRoute, navOptions)

fun NavGraphBuilder.showsListScreen(
    onProfileMenuOption: (ProfileMenuItem) -> Unit,
    onAddClick: () -> Unit,
    onShowClick: (String) -> Unit,
) {
    composable<ShowsListRoute> {
        ShowsListRoute(
            onProfileMenuOption = onProfileMenuOption,
            onAddClick = onAddClick,
            onShowClick = onShowClick,
        )
    }
}
