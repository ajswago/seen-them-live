package com.swago.seenthemlive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.swago.seenthemlive.data.util.NetworkMonitor
import com.swago.seenthemlive.ui.StlApp
import com.swago.seenthemlive.ui.screens.login.rememberLoginState
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val loginState = rememberLoginState(
                networkMonitor = networkMonitor
            )
            SeenThemLiveComposeTheme {
                StlApp(loginState = loginState)
            }
        }
    }
}
