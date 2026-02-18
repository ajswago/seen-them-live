package com.swago.seenthemlive.ui.screens.playlist

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.ui.components.cards.CreatePlaylistCard
import com.swago.seenthemlive.ui.components.listitems.LoadingSelectableShowListGroup
import com.swago.seenthemlive.ui.components.listitems.SelectableShowListGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreatePlaylistRoute(
    onBackClick: () -> Unit,
    onPlaylistConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreatePlaylistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val createPlaylistStep = viewModel.createPlaylistStep
    CreatePlaylistScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onCreatePlaylist = { name, selections ->  viewModel.createPlaylist(name = name, selections = selections) },
        onPlaylistConfirmed = onPlaylistConfirmed,
        createPlaylistStep = createPlaylistStep,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(
    uiState: CreatePlaylistUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onCreatePlaylist: ((String, List<String>) -> Unit) = { _, _ -> },
    onPlaylistConfirmed: () -> Unit = {},
    createPlaylistStep: CreatePlaylistStep = CreatePlaylistStep.NOT_STARTED
) {
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
                    IconButton(onClick = onBackClick) {
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
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                when (uiState) {
                    CreatePlaylistUiState.Loading -> {
                        CreatePlaylistCard(enabled = false)
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
                        Column {
                            for (i in 1..3) {
                                LoadingSelectableShowListGroup()
                                HorizontalDivider()
                            }
                        }
                    }
                    is CreatePlaylistUiState.Loaded -> {
                        val selections: MutableMap<String, Boolean> = remember {
                            mutableStateMapOf(*uiState.shows.map { it.id to false }.toTypedArray())
                        }
                        val groupedShows = uiState.shows.groupBy { it.date }
                        var enteredName: String? = null
                        CreatePlaylistCard(
                            onCreate = { name ->
                                enteredName = name
                                name?.let { name ->
                                    onCreatePlaylist(name, selections.filter { it.value }.keys.toList())
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
                        Column {
                            for (date in groupedShows.keys.toTypedArray()) {
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
                        if (createPlaylistStep != CreatePlaylistStep.NOT_STARTED) {
                            CreatePlaylistProgressDialog(
                                onDismissRequest = { onPlaylistConfirmed() },
                                onRetry = {
                                    enteredName?.let { name ->
                                        onCreatePlaylist(name, selections.filter { it.value }.keys.toList())
                                    }
                                },
                                step = createPlaylistStep
                            )
                        }
                    }
                }
            }
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
                .height(290.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    stringResource(R.string.create_playlist_progress_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp, start = 24.dp),
                )
                Spacer(modifier = Modifier.height(48.dp))
                LinearProgressIndicator(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    progress = { step.progress }
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = step.description,
                    minLines = 2,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (step == CreatePlaylistStep.FAILED) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                        ) {
                            Text(stringResource(R.string.cancel_button_label))
                        }
                        Button(
                            onClick = { onRetry() },
                        ) {
                            Text(stringResource(R.string.retry_button_label))
                        }
                    }
                } else if (step == CreatePlaylistStep.COMPLETE) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                        ) {
                            Text(stringResource(R.string.dismiss_button_label))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePlaylistScreenPreview() {
    val shows = listOf(
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
        uiState = CreatePlaylistUiState.Loaded(shows),
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
