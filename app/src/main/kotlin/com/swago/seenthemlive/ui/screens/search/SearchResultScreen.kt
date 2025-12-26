package com.swago.seenthemlive.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.ui.components.ListHeaderLabel
import com.swago.seenthemlive.ui.components.cards.LoadingShowCard
import com.swago.seenthemlive.ui.components.cards.ShowCard
import com.swago.seenthemlive.ui.components.listitems.LoadingTrackListItemNumbered
import com.swago.seenthemlive.ui.components.listitems.TrackListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SearchResultRoute(
    showId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel = hiltViewModel<SearchResultViewModel, SearchResultViewModel.Factory>(
        key = showId,
    ) { factory ->
        factory.create(showId)
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchResultScreen(
        uiState = uiState,
        onToggleSaved = {},
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    uiState: SearchResultUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    showToggleSaved: Boolean = true,
    onToggleSaved: (String) -> Unit = {},
) {
    Scaffold(
        topBar = { SearchResultAppBar(
            uiState = uiState,
            onBackClick = onBackClick,
            showToggleSaved = showToggleSaved,
            onToggleSaved = onToggleSaved,
        ) },
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
                ShowCard(uiState)
                Spacer(modifier = Modifier.height(16.dp))
                ListHeaderLabel(R.string.setlist_header)
                Spacer(modifier = Modifier.height(8.dp))
                TrackList(uiState = uiState)
                Spacer(modifier = Modifier.height(16.dp))
                EncoreTrackList(uiState = uiState)
            }
        }
    }
}

@Composable
fun ShowCard(
    uiState: SearchResultUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        SearchResultUiState.Loading -> {
            LoadingShowCard(modifier = modifier
                .padding(horizontal = 8.dp))
        }
        is SearchResultUiState.Loaded -> {
            val show = uiState.show
            ShowCard(
                artistName = show.artist,
                tourName = show.tourName,
                venueName = show.venueName,
                city = show.city,
                state = show.state,
                date = show.date,
                modifier = modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun TrackList(
    uiState: SearchResultUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        SearchResultUiState.Loading -> {
            LoadingTrackList(modifier = modifier)
        }
        is SearchResultUiState.Loaded -> {
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
            LoadingTrackListItemNumbered()
            HorizontalDivider()
        }
    }
}

@Composable
fun LoadedTrackList(
    uiState: SearchResultUiState.Loaded,
    modifier: Modifier = Modifier
) {
    val tracks = uiState.tracks
    Column(modifier = modifier) {
        for(track in tracks) {
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

@Composable
fun EncoreTrackList(
    uiState: SearchResultUiState,
    modifier: Modifier = Modifier
) {
    if (uiState is SearchResultUiState.Loaded && uiState.encoreTracks.isNotEmpty()) {
        val tracks = uiState.encoreTracks
        Column(modifier = modifier) {
            ListHeaderLabel("Encore")
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = modifier) {
                for(track in tracks) {
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

@Preview(showBackground = true)
@Composable
fun SearchResultScreenPreview() {
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
    val tracks = listOf(
        Track("Fury of the Storm", trackNumber = 1),
        Track("Cry Thunder", trackNumber = 2),
        Track("Power of the Triforce", trackNumber = 3),
        Track("The Last Dragonborn", trackNumber = 4),
        Track("Doomsday Party", trackNumber = 5),
        Track("My Heart Will Go On", trackNumber = 6, coverArtistName = "Celine Dion"),
    )
    val encore = listOf(
        Track("Through the Fire and Flames", trackNumber = 1)
    )
    SearchResultScreen(
        uiState = SearchResultUiState.Loaded(
            show = show,
            saved = false,
            tracks = tracks,
            encoreTracks = encore
        ),
        onToggleSaved = {},
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingSearchResultScreenPreview() {
    SearchResultScreen(
        uiState = SearchResultUiState.Loading,
        onToggleSaved = {},
    )
}
