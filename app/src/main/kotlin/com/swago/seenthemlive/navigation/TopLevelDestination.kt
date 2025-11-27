package com.swago.seenthemlive.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ViewTimeline
import androidx.compose.material.icons.outlined.History
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleText: String,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
//    SHOWS_LIST(
//        selectedIcon = Icons.Filled.History,
//        unselectedIcon = Icons.Outlined.History,
//        iconText = "Shows",
//        titleText = "Shows",
//        route = ShowsListRoute::class,
//        baseRoute = ShowsListBaseRoute::class
//    )
}
