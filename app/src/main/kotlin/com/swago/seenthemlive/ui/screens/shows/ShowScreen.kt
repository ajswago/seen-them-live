package com.swago.seenthemlive.ui.screens.shows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.ui.components.cards.LoadingShowCard
import com.swago.seenthemlive.ui.components.cards.ShowCard
import com.swago.seenthemlive.ui.components.listitems.ArtistListItem
import com.swago.seenthemlive.ui.components.listitems.FindMoreListItem
import com.swago.seenthemlive.ui.components.listitems.LoadingArtistListItemSimple
import com.swago.seenthemlive.ui.components.listitems.LoadingTrackListItemNumbered
import com.swago.seenthemlive.ui.components.listitems.TrackListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShowRoute(
    showId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowViewModel = hiltViewModel<ShowViewModel, ShowViewModel.Factory>(
        key = showId,
    ) { factory ->
        factory.create(showId)
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ShowScreen(
        uiState = uiState,
        onEditClicked = {},
        onToggleSaved = {},
        onArtistClicked = {},
        onFindMoreClicked = {},
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowScreen(
    uiState: ShowUiState,
    onEditClicked: () -> Unit,
    onToggleSaved: (String) -> Unit,
    onArtistClicked: (String) -> Unit,
    onFindMoreClicked: () -> Unit,
    modifier: Modifier = Modifier,
    showEdit: Boolean = false,
    showToggleSaved: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.show_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
                actions = {
                    if (showEdit) {
                        IconButton(onClick = { onEditClicked() }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = stringResource(R.string.edit_button_description)
                            )
                        }
                    }
                    if (showToggleSaved) {
                        when (uiState) {
                            ShowUiState.Loading -> {
                                IconButton(onClick = {}, enabled = false) {
                                    Icon(
                                        imageVector = Icons.Outlined.BookmarkAdd,
                                        contentDescription = stringResource(R.string.add_button_description)
                                    )
                                }
                            }
                            is ShowUiState.Loaded -> {
                                val show = uiState.show
                                if (!show.saved) {
                                    IconButton(onClick = { onToggleSaved(show.id) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.BookmarkAdd,
                                            contentDescription = stringResource(R.string.add_button_description)
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { onToggleSaved(show.id) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.BookmarkRemove,
                                            contentDescription = stringResource(R.string.remove_button_description)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
                when (uiState) {
                    ShowUiState.Loading -> {
                        LoadingShowCard()
                    }
                    is ShowUiState.Loaded -> {
                        val show = uiState.show
                        ShowCard(
                            artistName = show.artist,
                            tourName = show.tourName,
                            venueName = show.venueName,
                            city = show.city,
                            state = show.state,
                            date = show.date,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.also_at_show_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                when (uiState) {
                    ShowUiState.Loading -> {
                        LazyColumn {
                            items(2) {
                                LoadingArtistListItemSimple()
                                HorizontalDivider()
                            }
                        }
//                        FindMoreListItem(
//                            onClick = {},
//                            modifier = Modifier
//                                .height(55.dp)
//                        )
                    }
                    is ShowUiState.Loaded -> {
                        val linkedShows = uiState.linkedShows
                        LazyColumn {
                            items(linkedShows) { show ->
                                ArtistListItem(
                                    artistName = show.artist,
                                    onClick = { onArtistClicked(show.id) },
                                    modifier = Modifier
                                        .height(55.dp)
                                )
                                HorizontalDivider()
                            }
                        }
                        if (linkedShows.isNotEmpty()) {
                            FindMoreListItem(
                                onClick = onFindMoreClicked,
                                modifier = Modifier
                                    .height(55.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.setlist_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                when (uiState) {
                    ShowUiState.Loading -> {
                        LazyColumn {
                            items(4) {
                                LoadingTrackListItemNumbered()
                                HorizontalDivider()
                            }
                        }
                    }
                    is ShowUiState.Loaded -> {
                        val tracks = uiState.tracks
                        LazyColumn {
                            items(tracks) { track ->
                                TrackListItem(
                                    trackName = track.trackName,
                                    trackNumber = track.trackNumber,
                                    coverArtistName = track.coverArtistName,
                                    isTapeTrack = track.isTapeTrack,
                                    modifier = Modifier
                                        .height(55.dp)
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowScreenPreview() {
    val show = Show(
        id = "ID1",
        venueName = "The Fillmore Silver Spring",
        city = "Silver Spring",
        state = "MD",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        tourName = "Warp Speed Warriors",
        artist = "DragonForce"
    )
    val tracks = arrayOf(
        Track("Fury of the Storm", trackNumber = 1),
        Track("Cry Thunder", trackNumber = 2),
        Track("Power of the Triforce", trackNumber = 3),
        Track("The Last Dragonborn", trackNumber = 4),
        Track("Doomsday Party", trackNumber = 5),
        Track("My Heart Will Go On", trackNumber = 6, coverArtistName = "Celine Dion"),
        Track("Through the Fire and Flames", trackNumber = 7)
    )
    val linkedShows = arrayOf(
        Show(
            id = "ID1",
            artist = "Nekrogoblikon",
            city = "Silver Spring",
            state = "MD",
            venueName = "The Fillmore Silver Spring",
            date = Date()
        ),
        Show(
            id = "ID2",
            artist = "Dethklok",
            city = "Silver Spring",
            state = "MD",
            venueName = "The Fillmore Silver Spring",
            date = Date()
        )
    )
    ShowScreen(
        uiState = ShowUiState.Loaded(
            show = show,
            linkedShows = linkedShows,
            tracks = tracks
        ),
        onArtistClicked = {},
        onFindMoreClicked = {},
        onEditClicked = {},
        onToggleSaved = {},
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingShowScreenPreview() {
    ShowScreen(
        uiState = ShowUiState.Loading,
        onArtistClicked = {},
        onFindMoreClicked = {},
        onEditClicked = {},
        onToggleSaved = {},
    )
}
