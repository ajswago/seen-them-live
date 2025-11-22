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
import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.ui.components.cards.ArtistCard
import com.swago.seenthemlive.ui.components.listitems.GroupedShowListItem
import com.swago.seenthemlive.ui.components.listitems.TrackListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    artist: Artist,
    shows: Array<GroupedShow>,
    associatedArtists: Map<String, Array<String>>,
    tracks: Array<Track>,
    onShowClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Artist",
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
                ArtistCard(
                    artistName = artist.name,
                    lastShow = artist.lastShow,
                    showCount = shows.count(),
                    songCount = tracks.count(),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.shows_list_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .padding(start = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(shows) { show ->
                        GroupedShowListItem(
                            venueName = show.venueName,
                            city = show.city,
                            state = show.state,
                            date = show.date,
                            artistList = associatedArtists[show.id] ?: arrayOf(),
                            onClick = { onShowClicked(show.id) }
                        )
                        Divider()
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Songs",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .padding(start = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(tracks) { track ->
                        TrackListItem(
                            trackName = track.trackName,
                            trackCount = track.trackCount,
                            coverArtistName = track.coverArtistName
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
fun ArtistScreenPreview() {
    val shows = arrayOf(
        GroupedShow(
            id = "ID1",
            venueName = "Jiffy Lube Live",
            city = "Bristow",
            state = "VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date()
        ),
        GroupedShow(
            id = "ID2",
            venueName = "PPL Center",
            city = "Allentown",
            state = "PA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date()
        )
    )
    val associatedArtists = mapOf(
        shows[0].id to arrayOf("Anthrax", "Behemoth", "Slayer", "Testament"),
        shows[1].id to arrayOf("Megadeth", "Trivium")
    )
    val tracks = arrayOf(
        Track("Ruin", trackCount = 5),
        Track("Walk With Me in Hell", trackCount = 5),
        Track("Now You've Got Something to Die For", trackCount = 5),
        Track("Laid to Rest", trackCount = 5),
        Track("Redneck", trackCount = 3),
        Track("512", trackCount = 3)
    )
    val artist = Artist(
        id = "ID1",
        name = "Lamb of God",
        lastShow = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2022-06-10") ?: Date(),
    )
    ArtistScreen(
        artist = artist,
        shows = shows,
        associatedArtists = associatedArtists,
        tracks = tracks,
        onShowClicked = {}
    )
}
