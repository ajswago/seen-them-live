package com.swago.seenthemlive

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.swago.seenthemlive.navigation.HomeRoute
import com.swago.seenthemlive.navigation.RootNavHost
import com.swago.seenthemlive.ui.screens.login.LoginRoute
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var spotifyAuthCompletion: ((String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeenThemLiveComposeTheme {
                FirebaseApp.initializeApp(this)
                val currentUser = Firebase.auth.currentUser
                val startDestination = if (currentUser != null) {
                    HomeRoute
                } else {
                    LoginRoute
                }
                RootNavHost(
                    startDestination = startDestination,
                    googleSignOut = { googleSignOut() },
                    onSpotifyAuth = { authRequest, completion ->
                        spotifyAuthCompletion = completion
                        try {
                            AuthorizationClient.openLoginActivity(this, 1001, authRequest)
                        } catch (_: Exception) {}
                    }
                )
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (requestCode == 1001) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    spotifyAuthCompletion?.invoke(response.accessToken)
                }
                AuthorizationResponse.Type.ERROR -> {}
                else -> {}
            }
        }
    }

    fun googleSignOut() {
        Firebase.auth.signOut()
        lifecycleScope.launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                val credentialManager = CredentialManager.create(this@MainActivity)
                credentialManager.clearCredentialState(clearRequest)
            } catch (e: ClearCredentialException) {}
        }
    }
}
