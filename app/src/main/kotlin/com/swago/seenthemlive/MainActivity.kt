package com.swago.seenthemlive

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
import com.swago.seenthemlive.navigation.HomeRoute
import com.swago.seenthemlive.navigation.RootNavHost
import com.swago.seenthemlive.ui.screens.login.LoginRoute
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
                RootNavHost(startDestination = startDestination, googleSignOut = { googleSignOut() })
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
