package com.swago.seenthemlive.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.ui.components.cards.CreatePlaylistCard
import com.swago.seenthemlive.ui.components.listitems.SelectableShowListGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(
    shows: Array<Show>,
    onCreatePlaylist: (String, Array<String>) -> Unit,
    onPlaylistConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    createPlaylistStep: CreatePlaylistStep = CreatePlaylistStep.NOT_STARTED,
) {
    val selections = remember {
        mutableStateMapOf(*shows.map { it.id to false }.toTypedArray())
    }
    val groupedShows = shows.groupBy { it.date }
    var enteredName: String? = null
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.create_playlist_screen_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column {
                CreatePlaylistCard(
                    onCreate = { name ->
                        enteredName = name
                        name?.let { name ->
                            onCreatePlaylist(name, selections.filter { it.value }.keys.toTypedArray())
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.playlist_select_shows_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .padding(start = 24.dp)
                )
                Text(
                    text = stringResource(R.string.playlist_select_shows_secondary_label),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .padding(start = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(groupedShows.keys.toTypedArray()) { date ->
                        groupedShows[date]?.let { show ->
                            val artists = show.map{ it.artist }.toTypedArray()
                            val artistSelections = remember {
                                mutableStateMapOf(*artists.map { it to false }.toTypedArray())
                            }
                            SelectableShowListGroup(
                                venueName = show.first().venueName,
                                city = show.first().city,
                                state = show.first().state,
                                date = date,
                                artistList = artists,
                                selections = artistSelections,
                                onArtistSelected = { index, selected ->
                                    artistSelections[artists[index]] = selected
                                    selections[show[index].id] = selected
                                }
                            )
                        }
                    }
                }
            }
        }
        if (createPlaylistStep != CreatePlaylistStep.NOT_STARTED) {
            CreatePlaylistProgressDialog(
                onDismissRequest = { onPlaylistConfirmed() },
                onRetry = {
                    enteredName?.let { name ->
                        onCreatePlaylist(name, selections.filter { it.value }.keys.toTypedArray())
                    }
                },
                step = createPlaylistStep
            )
        }
    }
}

@Composable
fun CreatePlaylistProgressDialog(
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
    step: CreatePlaylistStep,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.create_playlist_progress_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(48.dp))
                LinearProgressIndicator(progress = step.progress)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = step.description,
                    minLines = 2,
                    maxLines = 2,
                    modifier = Modifier.padding(16.dp),
                )
                if (step == CreatePlaylistStep.FAILED) {
                    Row {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(stringResource(R.string.cancel_button_label))
                        }
                        TextButton(
                            onClick = { onRetry() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(stringResource(R.string.retry_button_label))
                        }
                    }
                } else if (step == CreatePlaylistStep.COMPLETE) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.dismiss_button_label))
                    }
                }
            }
        }
    }
}

enum class CreatePlaylistStep(var progress: Float, var description: String) {
    NOT_STARTED(0.0f, ""),
    CREATE_PLAYLIST(0.25f, "Creating new playlist in your Spotify account."),
    FIND_SONGS(0.5f, "Looking up selected songs."),
    ADD_SONGS(0.75f, "Adding matched songs to newly created playlist."),
    COMPLETE(1.0f, "Playlist created successfully. Enjoy listening on Spotify!"),
    FAILED(progress = 1.0f, "Failed to create playlist.")
}

@Preview(showBackground = true)
@Composable
fun CreatePlaylistScreenPreview() {
    val shows = arrayOf(
        Show(
            id = "ID1",
            artist = "Rodrigo y Gabriela",
            venueName = "Capital One Hall",
            city = "Tysons Corner",
            state = "VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date()
        ),
        Show(
            id = "ID2",
            artist = "Joe Satriani",
            venueName = "Warner Theater",
            city = "Washington",
            state = "DC",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-11") ?: Date()
        ),
        Show(
            id = "ID3",
            artist = "Steve Vai",
            venueName = "Warner Theater",
            city = "Washington",
            state = "DC",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-11") ?: Date()
        ),
        Show(
            id = "ID4",
            artist = "Dethklok",
            venueName = "The Fillmore Silver Spring",
            city = "Silver Spring",
            state = "MD",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-09") ?: Date()
        ),
        Show(
            id = "ID5",
            artist = "DragonForce",
            venueName = "The Fillmore Silver Spring",
            city = "Silver Spring",
            state = "MD",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-09") ?: Date()
        ),
        Show(
            id = "ID6",
            artist = "Nekrogoblikon",
            venueName = "The Fillmore Silver Spring",
            city = "Silver Spring",
            state = "MD",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-09") ?: Date()
        )
    )
    CreatePlaylistScreen(
        shows = shows,
        onCreatePlaylist = { name, showIds ->
        },
        onPlaylistConfirmed = {},
        createPlaylistStep = CreatePlaylistStep.NOT_STARTED
    )
}

@Preview(showBackground = true)
@Composable
fun CreatePlaylistProgressDialogPreview() {
    CreatePlaylistProgressDialog(
        onDismissRequest = {},
        onRetry = {},
        step = CreatePlaylistStep.CREATE_PLAYLIST
    )
}

@Preview(showBackground = true)
@Composable
fun CreatePlaylistProgressDialogSuccessPreview() {
    CreatePlaylistProgressDialog(
        onDismissRequest = {},
        onRetry = {},
        step = CreatePlaylistStep.COMPLETE
    )
}

@Preview(showBackground = true)
@Composable
fun CreatePlaylistProgressDialogFailedPreview() {
    CreatePlaylistProgressDialog(
        onDismissRequest = {},
        onRetry = {},
        step = CreatePlaylistStep.FAILED
    )
}
