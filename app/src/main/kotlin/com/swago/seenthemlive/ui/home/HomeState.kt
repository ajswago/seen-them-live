package com.swago.seenthemlive.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.swago.seenthemlive.navigation.TopLevelDestination
import com.swago.seenthemlive.ui.screens.artists.navigateToArtistList
import com.swago.seenthemlive.ui.screens.map.navigateToMap
import com.swago.seenthemlive.ui.screens.shows.navigateToShowsList
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberHomeState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): HomeState {
    return remember(
        navController,
        coroutineScope
    ) {
        HomeState(
            navController = navController,
            coroutineScope = coroutineScope
        )
    }
}

@Stable
class HomeState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    var currentTopLevelDestination = mutableStateOf<TopLevelDestination?>(TopLevelDestination.SHOWS_LIST)

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        currentTopLevelDestination.value = topLevelDestination
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.id) {
                inclusive = true
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.SHOWS_LIST -> navController.navigateToShowsList(topLevelNavOptions)
            TopLevelDestination.ARTIST_LIST -> navController.navigateToArtistList(topLevelNavOptions)
            TopLevelDestination.MAP -> navController.navigateToMap(topLevelNavOptions)
        }
    }
}
