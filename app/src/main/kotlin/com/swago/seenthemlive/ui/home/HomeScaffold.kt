package com.swago.seenthemlive.ui.home

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swago.seenthemlive.navigation.HomeNavHost

@Composable
fun HomeRoute(
    logout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeScaffold(
        homeState = rememberHomeState(),
        logout = logout,
        modifier = modifier
    )
}

@Composable
fun HomeScaffold(
    homeState: HomeState,
    logout: () -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val currentTopLevelDestination = homeState.currentTopLevelDestination
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            homeState.topLevelDestinations.forEach { destination ->
                val selected = destination == currentTopLevelDestination.value
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
            logout = logout,
            modifier = modifier
        )
    }
}
