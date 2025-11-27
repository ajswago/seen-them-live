package com.swago.seenthemlive.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.swago.seenthemlive.ui.StlAppState
import com.swago.seenthemlive.ui.screens.login.LoginRoute
import com.swago.seenthemlive.ui.screens.login.loginScreen

@Composable
fun StlNavHost(
    appState: StlAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier
    ) {
        loginScreen({})
    }
}
