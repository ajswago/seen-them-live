package com.swago.seenthemlive.ui.screens.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.ui.components.ListHeaderLabel
import com.swago.seenthemlive.ui.components.cards.ArtistCard
import com.swago.seenthemlive.ui.components.cards.LoadingArtistCard
import com.swago.seenthemlive.ui.components.listitems.GroupedShowListItem
import com.swago.seenthemlive.ui.components.listitems.LoadingGroupedShowListItem
import com.swago.seenthemlive.ui.components.listitems.LoadingTrackListItemCount
import com.swago.seenthemlive.ui.components.listitems.TrackListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ArtistRoute(
    artistId: String,
    onBackClick: () -> Unit,
    onShowClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArtistViewModel = hiltViewModel<ArtistViewModel, ArtistViewModel.Factory>(
        key = artistId
    ) { factory ->
        factory.create(artistId)
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArtistScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onShowClick = onShowClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    uiState: ArtistUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onShowClick: (String) -> Unit = {},
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
                ArtistCard(
                    uiState = uiState
                )
                Spacer(modifier = Modifier.height(16.dp))
                ListHeaderLabel(R.string.shows_list_header)
                Spacer(modifier = Modifier.height(8.dp))
                ShowsList(
                    uiState = uiState,
                    onShowClicked = onShowClick
                )
                Spacer(modifier = Modifier.height(8.dp))
                ListHeaderLabel("Songs")
                Spacer(modifier = Modifier.height(8.dp))
                TrackList(uiState = uiState)
            }
        }
    }
}

@Composable
fun ArtistCard(
    uiState: ArtistUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ArtistUiState.Loading -> {
            LoadingArtistCard(modifier = modifier
                .padding(horizontal = 8.dp))
        }
        is ArtistUiState.Loaded -> {
            val artist = uiState.artist
            val shows = uiState.shows
            val tracks = uiState.tracks
            ArtistCard(
                artistName = artist.name,
                lastShow = artist.lastShow,
                showCount = shows.count(),
                songCount = tracks.count(),
                modifier = modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ShowsList(
    uiState: ArtistUiState,
    modifier: Modifier = Modifier,
    onShowClicked: (String) -> Unit = {},
) {
    when (uiState) {
        ArtistUiState.Loading -> {
            LoadingShowsList(modifier = modifier)
        }
        is ArtistUiState.Loaded -> {
            LoadedShowsList(
                uiState = uiState,
                onShowClicked = onShowClicked,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LoadingShowsList(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (i in 1..2) {
            LoadingGroupedShowListItem()
            HorizontalDivider()
        }
    }
}

@Composable
fun LoadedShowsList(
    uiState: ArtistUiState.Loaded,
    modifier: Modifier = Modifier,
    onShowClicked: (String) -> Unit = {},
) {
    Column(modifier = modifier) {
        val shows = uiState.shows
        for (show in shows.sortedByDescending { it.date }) {
            GroupedShowListItem(
                venueName = show.venueName,
                city = show.city,
                state = show.state,
                date = show.date,
                artistList = show.artists,
                onClick = { onShowClicked(show.id) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun TrackList(
    uiState: ArtistUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ArtistUiState.Loading -> {
            LoadingTrackList(modifier = modifier)
        }
        is ArtistUiState.Loaded -> {
            LoadedTrackList(
                uiState = uiState,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LoadingTrackList(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (i in 1..4) {
            LoadingTrackListItemCount()
            HorizontalDivider()
        }
    }
}

@Composable
fun LoadedTrackList(
    uiState: ArtistUiState.Loaded,
    modifier: Modifier = Modifier
) {
    val tracks = uiState.tracks
    Column(modifier = modifier) {
        for (track in tracks.sortedBy{ it.trackName }.sortedByDescending { it.trackCount }) {
            TrackListItem(
                trackName = track.trackName,
                trackCount = track.trackCount,
                coverArtistName = track.coverArtistName
            )
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistScreenPreview() {
    val shows = listOf(
        GroupedShow(
            id = "ID1",
            venueName = "Jiffy Lube Live",
            city = "Bristow",
            state = "VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date(),
            artists = listOf("Anthrax", "Behemoth", "Slayer", "Testament")
        ),
        GroupedShow(
            id = "ID2",
            venueName = "PPL Center",
            city = "Allentown",
            state = "PA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            listOf("Megadeth", "Trivium")
        )
    )
    val tracks = listOf(
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
        uiState = ArtistUiState.Loaded(
            artist = artist,
            shows = shows,
            tracks = tracks
        ),
        onShowClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingArtistScreen() {
    ArtistScreen(
        uiState = ArtistUiState.Loading,
        onShowClick = {}
    )
}
