package com.swago.seenthemlive.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.swago.seenthemlive.ui.home.HomeState
import com.swago.seenthemlive.ui.screens.artists.artistListScreen
import com.swago.seenthemlive.ui.screens.artists.artistScreen
import com.swago.seenthemlive.ui.screens.artists.navigateToArtist
import com.swago.seenthemlive.ui.screens.map.mapScreen
import com.swago.seenthemlive.ui.screens.search.navigateToSearch
import com.swago.seenthemlive.ui.screens.search.searchScreen
import com.swago.seenthemlive.ui.screens.shows.ShowsListRoute
import com.swago.seenthemlive.ui.screens.shows.navigateToShow
import com.swago.seenthemlive.ui.screens.shows.showScreen
import com.swago.seenthemlive.ui.screens.shows.showsListScreen

@Composable
fun HomeNavHost(
    homeState: HomeState,
    modifier: Modifier = Modifier
) {
    val navController = homeState.navController
    NavHost(
        navController = navController,
        startDestination = ShowsListRoute,
        modifier = modifier
    ) {
        showsListScreen(
            onAddClick = navController::navigateToSearch,
            onShowClick = { navController.navigateToShow(it) }
        )
        artistListScreen(
            onArtistClick = { navController.navigateToArtist(it) }
        )
        mapScreen()
        searchScreen(
            onShowClick = { navController.navigateToShow(it) },
            onBackClick = navController::popBackStack
        )
        showScreen(onBackClick = navController::popBackStack)
        artistScreen(onBackClick = navController::popBackStack)
    }
}
