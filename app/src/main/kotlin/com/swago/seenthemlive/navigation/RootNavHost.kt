package com.swago.seenthemlive.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.swago.seenthemlive.ui.screens.login.LoginRoute

@Composable
fun RootNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier
    ) {
        loginNav(navController = navController)

        homeNavHost(
            logout = {
                navController.navigate(LoginRoute) {
                    popUpTo(0){}
                }
            }
        )
    }
}
