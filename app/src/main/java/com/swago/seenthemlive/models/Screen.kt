package com.swago.seenthemlive.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Map
import androidx.compose.ui.graphics.vector.ImageVector


interface Screen {
    val icon: ImageVector
    val label: String
    val route: String
}

object Shows : Screen {
    override val icon = Icons.Outlined.Event
    override val label = "Shows"
    override val route = "shows"
}

object Artists : Screen {
    override val icon = Icons.Outlined.Groups
    override val label = "Artists"
    override val route = "artists"
}

object Map : Screen {
    override val icon = Icons.Outlined.Map
    override val label = "Map"
    override val route = "map"
}

val screens = listOf(Shows, Artists, Map)
