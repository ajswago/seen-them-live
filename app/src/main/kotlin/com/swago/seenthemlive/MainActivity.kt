package com.swago.seenthemlive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.swago.seenthemlive.data.util.NetworkMonitor
import com.swago.seenthemlive.navigation.RootNavHost
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
            SeenThemLiveComposeTheme {
                RootNavHost()
            }
        }
    }
}
