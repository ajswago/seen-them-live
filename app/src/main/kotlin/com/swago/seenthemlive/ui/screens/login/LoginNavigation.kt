package com.swago.seenthemlive.ui.screens.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseUser
import kotlinx.serialization.Serializable

@Serializable object LoginRoute

fun NavGraphBuilder.loginScreen(
    onLogin: (FirebaseUser?) -> Unit
) {
    composable<LoginRoute> {
        LoginRoute(onLogin = onLogin)
    }
}
