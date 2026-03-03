package com.swago.seenthemlive.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.swago.seenthemlive.ui.screens.login.LoginRoute

@Composable
fun RootNavHost(
    modifier: Modifier = Modifier,
    startDestination: Any = LoginRoute,
    googleSignOut: () -> Unit,
    onSpotifyAuth: (AuthorizationRequest, (String) -> Unit) -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginNav(navController = navController)

        homeNavHost(
            logout = {
                googleSignOut()
                navController.navigate(LoginRoute) {
                    popUpTo(0){}
                }
            },
            onSpotifyAuth = onSpotifyAuth
        )
    }
}
