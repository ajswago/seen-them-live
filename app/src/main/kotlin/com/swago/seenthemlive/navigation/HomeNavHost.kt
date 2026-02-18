package com.swago.seenthemlive.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.swago.seenthemlive.ui.components.ProfileMenuItem
import com.swago.seenthemlive.ui.home.HomeState
import com.swago.seenthemlive.ui.screens.about.aboutScreen
import com.swago.seenthemlive.ui.screens.about.navigateToAbout
import com.swago.seenthemlive.ui.screens.artists.artistListScreen
import com.swago.seenthemlive.ui.screens.artists.artistScreen
import com.swago.seenthemlive.ui.screens.artists.navigateToArtist
import com.swago.seenthemlive.ui.screens.map.mapScreen
import com.swago.seenthemlive.ui.screens.playlist.createPlaylistScreen
import com.swago.seenthemlive.ui.screens.playlist.navigateToCreatePlaylist
import com.swago.seenthemlive.ui.screens.profile.navigateToProfile
import com.swago.seenthemlive.ui.screens.profile.profileScreen
import com.swago.seenthemlive.ui.screens.search.navigateToSearch
import com.swago.seenthemlive.ui.screens.search.navigateToSearchResult
import com.swago.seenthemlive.ui.screens.search.searchResultScreen
import com.swago.seenthemlive.ui.screens.search.searchScreen
import com.swago.seenthemlive.ui.screens.shows.ShowsListRoute
import com.swago.seenthemlive.ui.screens.shows.navigateToShow
import com.swago.seenthemlive.ui.screens.shows.showScreen
import com.swago.seenthemlive.ui.screens.shows.showsListScreen

@Composable
fun HomeNavHost(
    homeState: HomeState,
    logout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = homeState.navController
    NavHost(
        navController = navController,
        startDestination = ShowsListRoute,
        modifier = modifier
    ) {
        showsListScreen(
            onProfileMenuOption = {
                onProfileMenuOption(
                    it,
                    navController = navController,
                    logout = logout
                ) },
            onAddClick = navController::navigateToSearch,
            onShowClick = { navController.navigateToShow(it) }
        )
        artistListScreen(
            onProfileMenuOption = {
                onProfileMenuOption(
                    it,
                    navController = navController,
                    logout = logout
                ) },
            onArtistClick = { navController.navigateToArtist(it) }
        )
        mapScreen(
            onProfileMenuOption = {
                onProfileMenuOption(
                    it,
                    navController = navController,
                    logout = logout
                ) },
            )
        searchScreen(
            onShowClick = { navController.navigateToSearchResult(it) },
            onBackClick = navController::popBackStack
        )
        searchResultScreen(onBackClick = navController::popBackStack)
        showScreen(
            onBackClick = navController::popBackStack,
            onArtistClick = { navController.navigateToShow(it) },
            onRelatedShowResultClick = { navController.navigateToSearchResult(it) },
        )
        artistScreen(
            onBackClick = navController::popBackStack,
            onShowClick = { navController.navigateToShow(it) }
        )
        profileScreen(
            onBackClick = navController::popBackStack,
            onArtistClick = { navController.navigateToArtist(it) }
        )
        createPlaylistScreen(
            onBackClick = navController::popBackStack,
            onPlaylistConfirmed = navController::popBackStack
        )
        aboutScreen(
            onBackClick = navController::popBackStack,
        )
    }
}

fun onProfileMenuOption(
    item: ProfileMenuItem,
    navController: NavController,
    logout: () -> Unit
) {
    when (item) {
        ProfileMenuItem.PROFILE -> {
            navController.navigateToProfile()
        }
        ProfileMenuItem.CREATE_PLAYLIST -> {
            navController.navigateToCreatePlaylist()
        }
        ProfileMenuItem.LOGOUT -> {
            logout()
        }
        ProfileMenuItem.ABOUT -> {
            navController.navigateToAbout()
        }
    }
}
