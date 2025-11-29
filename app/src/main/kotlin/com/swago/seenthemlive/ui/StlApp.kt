package com.swago.seenthemlive.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swago.seenthemlive.navigation.LoginNavHost
import com.swago.seenthemlive.ui.screens.login.LoginState

@Composable
fun StlApp(
    loginState: LoginState,
    modifier: Modifier = Modifier
) {
    LoginNavHost(
        loginState = loginState, modifier = modifier
    )
}
