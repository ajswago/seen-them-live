package com.swago.seenthemlive.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.ui.screens.LoginScreen

@Composable
fun StlApp(
    appState: StlAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    LoginScreen(
        onLogin = {},
        isOffline = isOffline
    )
}
