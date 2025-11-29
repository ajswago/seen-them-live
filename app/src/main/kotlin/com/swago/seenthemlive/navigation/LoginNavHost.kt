package com.swago.seenthemlive.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.swago.seenthemlive.ui.screens.login.LoginState
import com.swago.seenthemlive.ui.screens.login.LoginRoute
import com.swago.seenthemlive.ui.screens.login.loginScreen

@Composable
fun LoginNavHost(
    loginState: LoginState,
    modifier: Modifier = Modifier
) {
    val navController = loginState.navController
    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier
    ) {
        loginScreen(
            onLogin = { navController.navigateToHomeNavHost(navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }) }
        )
        homeNavHost()
    }
}
