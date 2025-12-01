package com.swago.seenthemlive.ui.home

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.swago.seenthemlive.navigation.HomeNavHost

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
) {
    HomeScaffold(
        homeState = rememberHomeState(),
        modifier = modifier
    )
}

@Composable
fun HomeScaffold(
    homeState: HomeState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            homeState.topLevelDestinations.forEach { destination ->
                val selected = homeState.navController
                    .isRouteInBackStack(destination.baseRoute.qualifiedName)
                item(
                    selected = selected,
                    onClick = { homeState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(destination.iconText) }
                )
            }
        },
        layoutType = NavigationSuiteScaffoldDefaults
            .calculateFromAdaptiveInfo(windowAdaptiveInfo)
    ) {

        HomeNavHost(
            homeState = homeState,
            modifier = modifier
        )
    }
}

fun NavController.isRouteInBackStack(routeToCheck: String?): Boolean {
    return this.currentBackStack.value.any { entry ->
        entry.destination.route == routeToCheck
    }
}
