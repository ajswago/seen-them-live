package com.swago.seenthemlive.ui.screens

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
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.ui.components.cards.ShowCard
import com.swago.seenthemlive.ui.components.listitems.ArtistListItem
import com.swago.seenthemlive.ui.components.listitems.FindMoreListItem
import com.swago.seenthemlive.ui.components.listitems.TrackListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowScreen(
    show: Show,
    alsoAtThisShowArtists: Array<String>,
    tracks: Array<Track>,
    onEditClicked: () -> Unit,
    onRemoveClicked: (Show) -> Unit,
    onArtistClicked: (String) -> Unit,
    onFindMoreClicked: () -> Unit,
    modifier: Modifier = Modifier
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
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClicked() }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.edit_button_description)
                        )
                    }
                    IconButton(onClick = { onRemoveClicked(show) }) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkRemove,
                            contentDescription = stringResource(R.string.remove_button_description)
                        )
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
                ShowCard(
                    artistName = show.artist,
                    tourName = show.tourName,
                    venueName = show.venueName,
                    locationName = show.locationName,
                    date = show.date,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.also_at_show_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(alsoAtThisShowArtists) { artist ->
                        ArtistListItem(
                            artistName = artist,
                            onClick = { onArtistClicked(show.artist) },
                            modifier = Modifier
                                .height(55.dp)
                        )
                        Divider()
                    }
                }
                FindMoreListItem(onClick = onFindMoreClicked,
                    modifier = Modifier
                        .height(55.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.setlist_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                        Divider()
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
        venueName = "The Fillmore Silver Spring",
        locationName = "Silver Spring, Maryland",
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
    ShowScreen(
        show = show,
        alsoAtThisShowArtists = arrayOf("Nekrogoblikon", "Dethklok"),
        tracks = tracks,
        onArtistClicked = {},
        onFindMoreClicked = {},
        onEditClicked = {},
        onRemoveClicked = {},
        modifier = Modifier
    )
}