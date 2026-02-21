package com.swago.seenthemlive.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.swago.seenthemlive.ui.screens.login.loginScreen

fun NavGraphBuilder.loginNav(
    navController: NavHostController
) {
    loginScreen(
        onLogin = { user ->
            if (user != null) {
                navController.navigateToHomeNavHost(navOptions {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                })
            }
        }
    )
}
