package com.swago.seenthemlive

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.ui.screens.ShowsListScreen
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeenThemLiveComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                    val shows = arrayOf(
//                        Show(
//                            id = "ID1",
//                            artist = "Rodrigo y Gabriela",
//                            venueName = "Capital One Hall",
//                            city = "Tysons Corner",
//                            state = "VA",
//                            date = SimpleDateFormat(
//                                "yyyy-MM-dd", Locale.US
//                            ).parse("2022-06-10") ?: Date()
//                        ),
//                        Show(
//                            id = "ID2",
//                            artist = "Joe Satriani",
//                            venueName = "Warner Theater",
//                            city = "Washington",
//                            state = "DC",
//                            date = SimpleDateFormat(
//                                "yyyy-MM-dd", Locale.US
//                            ).parse("2024-04-11") ?: Date()
//                        ),
//                        Show(
//                            id = "ID3",
//                            artist = "Steve Vai",
//                            venueName = "Warner Theater",
//                            city = "Washington",
//                            state = "DC",
//                            date = SimpleDateFormat(
//                                "yyyy-MM-dd", Locale.US
//                            ).parse("2024-04-11") ?: Date()
//                        ),
//                        Show(
//                            id = "ID4",
//                            artist = "Dethklok",
//                            venueName = "The Fillmore Silver Spring",
//                            city = "Silver Spring",
//                            state = "MD",
//                            date = SimpleDateFormat(
//                                "yyyy-MM-dd", Locale.US
//                            ).parse("2024-04-09") ?: Date()
//                        ),
//                        Show(
//                            id = "ID5",
//                            artist = "DragonForce",
//                            venueName = "The Fillmore Silver Spring",
//                            city = "Silver Spring",
//                            state = "MD",
//                            date = SimpleDateFormat(
//                                "yyyy-MM-dd", Locale.US
//                            ).parse("2024-04-09") ?: Date()
//                        ),
//                        Show(
//                            id = "ID6",
//                            artist = "Nekrogoblikon",
//                            venueName = "The Fillmore Silver Spring",
//                            city = "Silver Spring",
//                            state = "MD",
//                            date = SimpleDateFormat(
//                                "yyyy-MM-dd", Locale.US
//                            ).parse("2024-04-09") ?: Date()
//                        )
//                    )
//                    val coroutineScope = rememberCoroutineScope()
//                    var status by remember { mutableStateOf(CreatePlaylistStep.NOT_STARTED) }
//                    CreatePlaylistScreen(
//                        shows = shows,
//                        onCreatePlaylist = { name, showIds ->
//                            status = CreatePlaylistStep.CREATE_PLAYLIST
//                            coroutineScope.launch {
//                                delay(Duration.ofMillis(2000))
//                                status = CreatePlaylistStep.FIND_SONGS
//                                delay(Duration.ofMillis(2000))
//                                status = CreatePlaylistStep.ADD_SONGS
//                                delay(Duration.ofMillis(2000))
//                                status = CreatePlaylistStep.COMPLETE
//                            }
//                        },
//                        onPlaylistConfirmed = {
//                            status = CreatePlaylistStep.NOT_STARTED
//                        },
//                        createPlaylistStep = status
//                    )

//                    SearchCard(
//                        onSearch = {}
//                    )

//                    AppBarWithProfile(title = "Test") { }

                    val shows = arrayOf(
                        Show(
                            id="ID1",
                            venueName = "Capital One Hall",
                            city = "Tysons Corner",
                            state = "VA",
                            date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.US
                            ).parse("2022-06-10") ?: Date(),
                            artist="Rodrigo y Gabriela"
                        ),
                        Show(
                            id="ID2",
                            venueName = "Warner Theatre",
                            city = "Washington",
                            state = "DC",
                            date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.US
                            ).parse("2024-04-11") ?: Date(),
                            artist = "Joe Satriani"
                        ),
                        Show(
                            id="ID3",
                            venueName = "Warner Theatre",
                            city = "Washington",
                            state = "DC",
                            date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.US
                            ).parse("2024-04-11") ?: Date(),
                            artist = "Steve Vai"
                        ),
                        Show(
                            id = "ID4",
                            venueName = "The Fillmore Silver Spring",
                            city = "Silver Spring",
                            state = "MD",
                            date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.US
                            ).parse("2024-04-09") ?: Date(),
                            artist = "Dethklok"
                        ),
                        Show(
                            id = "ID5",
                            venueName = "The Fillmore Silver Spring",
                            city = "Silver Spring",
                            state = "MD",
                            date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.US
                            ).parse("2024-04-09") ?: Date(),
                            artist = "DragonForce"
                        ),
                        Show(
                            id = "ID6",
                            venueName = "The Fillmore Silver Spring",
                            city = "Silver Spring",
                            state = "MD",
                            date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.US
                            ).parse("2024-04-09") ?: Date(),
                            artist = "Nekrogoblikon"
                        )
                    )
                    ShowsListScreen(
                        shows = shows,
                        onProfileMenuOption = { Log.d("TESTING", "Option clicked: $it") },
                        onShowClicked = { Log.d("TESTING", "Show clicked: $it") },
                        onAddButton = { Log.d("TESTING", "Add button clicked") },
                        modifier = Modifier
                    )
                }
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