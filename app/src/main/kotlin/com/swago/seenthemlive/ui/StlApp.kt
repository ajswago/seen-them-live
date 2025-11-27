package com.swago.seenthemlive.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swago.seenthemlive.navigation.StlNavHost

@Composable
fun StlApp(
    appState: StlAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
//            appState.topLevelDestinations
        }
    ) {
        StlNavHost(
            appState = appState
        )
    }
}
