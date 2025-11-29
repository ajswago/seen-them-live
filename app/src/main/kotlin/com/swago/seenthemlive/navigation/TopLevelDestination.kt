package com.swago.seenthemlive.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Map
import androidx.compose.ui.graphics.vector.ImageVector
import com.swago.seenthemlive.ui.screens.artists.ArtistListRoute
import com.swago.seenthemlive.ui.screens.map.MapRoute
import com.swago.seenthemlive.ui.screens.shows.ShowsListRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleText: String,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    SHOWS_LIST(
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        iconText = "Shows",
        titleText = "Shows",
        route = ShowsListRoute::class,
        baseRoute = ShowsListRoute::class
    ),
    ARTIST_LIST(
        selectedIcon = Icons.Filled.Group,
        unselectedIcon = Icons.Outlined.Group,
        iconText = "Artists",
        titleText = "Artists",
        route = ArtistListRoute::class,
        baseRoute = ArtistListRoute::class
    ),
    MAP(
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
        iconText = "Map",
        titleText = "Map",
        route = MapRoute::class,
        baseRoute = MapRoute::class
    )
}
