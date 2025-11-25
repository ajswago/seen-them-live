package com.swago.seenthemlive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.data.util.NetworkMonitor
import com.swago.seenthemlive.ui.StlApp
import com.swago.seenthemlive.ui.rememberStlAppState
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
            val appState = rememberStlAppState(
                networkMonitor = networkMonitor
            )
            SeenThemLiveComposeTheme {
                StlApp(appState = appState)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SeenThemLiveComposeTheme {
        Greeting("Android")
    }
}