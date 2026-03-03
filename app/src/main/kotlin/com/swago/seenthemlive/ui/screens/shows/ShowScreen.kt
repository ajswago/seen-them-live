package com.swago.seenthemlive.ui.screens.shows

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.ui.components.ListHeaderLabel
import com.swago.seenthemlive.ui.components.cards.LoadingShowCard
import com.swago.seenthemlive.ui.components.cards.ShowCard
import com.swago.seenthemlive.ui.components.listitems.ArtistListItem
import com.swago.seenthemlive.ui.components.listitems.FindMoreListItem
import com.swago.seenthemlive.ui.components.listitems.LoadingArtistListItemSimple
import com.swago.seenthemlive.ui.components.listitems.LoadingShowListItem
import com.swago.seenthemlive.ui.components.listitems.LoadingTrackListItemNumbered
import com.swago.seenthemlive.ui.components.listitems.ShowListItem
import com.swago.seenthemlive.ui.components.listitems.TrackListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShowRoute(
    showId: String,
    onBackClick: () -> Unit,
    onArtistClick: (String) -> Unit,
    onRelatedShowResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowViewModel = hiltViewModel<ShowViewModel, ShowViewModel.Factory>(
        key = showId,
    ) { factory ->
        factory.create(showId)
    }
) {
    val uiState = viewModel.uiState
    val relatedShowsResultsUiState = viewModel.relatedShowsResultsUiState
    val confirmationUiState = viewModel.confirmationUiState
    ShowScreen(
        uiState = uiState,
        relatedShowsResultsUiState = relatedShowsResultsUiState,
        confirmationUiState = confirmationUiState,
        onDismissResults = { viewModel.dismissBottomSheet() },
        onEditClicked = {},
        onToggleSaved = { completion ->
            viewModel.toggleSaved(completion)
        },
        onFindMoreClicked = {
            if (uiState is ShowUiState.Loaded) {
                viewModel.performSearch(uiState.show.date, uiState.show.venueName)
            }
        },
        onBackClick = onBackClick,
        onArtistClick = onArtistClick,
        onRelatedShowResultClick = onRelatedShowResultClick,
        refresh = { viewModel.load() },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowScreen(
    uiState: ShowUiState,
    relatedShowsResultsUiState: RelatedShowsResultsUiState,
    confirmationUiState: ConfirmationUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onDismissResults: () -> Unit = {},
    showToggleSaved: Boolean = true,
    showEdit: Boolean = false,
    onToggleSaved: (completion: () -> Unit) -> Unit = {},
    onEditClicked: () -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    onRelatedShowResultClick: (String) -> Unit = {},
    onFindMoreClicked: () -> Unit = {},
    refresh: () -> Unit = {}
) {
    var showConfirmationDialog by remember {mutableStateOf(false) }
    Scaffold(
        topBar = { ShowAppBar(
            uiState = uiState,
            onBackClick = onBackClick,
            showToggleSaved = showToggleSaved,
            showEdit = showEdit,
            onToggleSaved = { showConfirmationDialog = true },
            onEditClicked = onEditClicked
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
                ListHeaderLabel(R.string.also_at_show_header)
                Spacer(modifier = Modifier.height(8.dp))
                LinkedShowsList(
                    uiState = uiState,
                    onArtistClick = onArtistClick,
                    onFindMoreClicked = onFindMoreClicked
                )
                Spacer(modifier = Modifier.height(16.dp))
                ListHeaderLabel(R.string.setlist_header)
                Spacer(modifier = Modifier.height(8.dp))
                TrackList(uiState = uiState)
                Spacer(modifier = Modifier.height(16.dp))
                EncoreTrackList(uiState = uiState)
                RelatedShowsResults(
                    relatedShowsResultsUiState,
                    onShowClick = onRelatedShowResultClick,
                    onDismissResults = onDismissResults)
                if (showConfirmationDialog && uiState is ShowUiState.Loaded) {
                    ConfirmAddRemoveDialog(
                        uiState = confirmationUiState,
                        onDismissRequest = { showConfirmationDialog = false },
                        onConfirm = {
                            onToggleSaved {
                                showConfirmationDialog = false
                            }
                        },
                        saved = uiState.saved
                    )
                }
            }
        }
    }
}

@Composable
fun ShowCard(
    uiState: ShowUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ShowUiState.Loading -> {
            LoadingShowCard(modifier = modifier
                .padding(horizontal = 8.dp))
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
                modifier = modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun LinkedShowsList(
    uiState: ShowUiState,
    onArtistClick: (String) -> Unit,
    onFindMoreClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ShowUiState.Loading -> {
            LoadingLinkedShowsList(modifier = modifier)
        }
        is ShowUiState.Loaded -> {
            LoadedLinkedShowsList(
                uiState = uiState,
                onArtistClick = onArtistClick,
                onFindMoreClicked = onFindMoreClicked,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LoadingLinkedShowsList(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (i in 1..2) {
            LoadingArtistListItemSimple()
            HorizontalDivider()
        }
    }
    FindMoreListItem(
        enabled = false,
        onClick = {},
        modifier = Modifier
            .height(55.dp)
    )
    HorizontalDivider()
}

@Composable
fun LoadedLinkedShowsList(
    uiState: ShowUiState.Loaded,
    modifier: Modifier = Modifier,
    onArtistClick: (String) -> Unit = {},
    onFindMoreClicked: () -> Unit = {},
) {
    val linkedShows = uiState.linkedShows
    Column(modifier = modifier) {
        for(show in linkedShows) {
            ArtistListItem(
                artistName = show.artist,
                onClick = { onArtistClick(show.id) },
                modifier = Modifier
                    .height(55.dp)
            )
            HorizontalDivider()
        }
    }
    FindMoreListItem(
        onClick = onFindMoreClicked,
        modifier = Modifier
            .height(55.dp)
    )
    HorizontalDivider()
}

@Composable
fun TrackList(
    uiState: ShowUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ShowUiState.Loading -> {
            LoadingTrackList(modifier = modifier)
        }
        is ShowUiState.Loaded -> {
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
        for(i in 1..4) {
            LoadingTrackListItemNumbered()
            HorizontalDivider()
        }
    }
}

@Composable
fun LoadedTrackList(
    uiState: ShowUiState.Loaded,
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
    uiState: ShowUiState,
    modifier: Modifier = Modifier
) {
    if (uiState is ShowUiState.Loaded && uiState.encoreTracks.isNotEmpty()) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatedShowsResults(
    relatedShowsResultsUiState: RelatedShowsResultsUiState,
    onShowClick: (String) -> Unit = {},
    onDismissResults: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    if (relatedShowsResultsUiState != RelatedShowsResultsUiState.Hidden) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismissResults()
            },
            sheetState = sheetState
        ) {
            when (relatedShowsResultsUiState) {
                RelatedShowsResultsUiState.Hidden -> {}
                RelatedShowsResultsUiState.Loading -> {
                    LoadingRelatedShowResults()
                }
                is RelatedShowsResultsUiState.Results -> {
                    LoadedRelatedShowResults(
                        relatedShowsResultsUiState,
                        onShowClick = onShowClick
                    )
                }
            }
        }
    }
}

@Composable fun LoadingRelatedShowResults(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (i in 1..2) {
            LoadingShowListItem()
            HorizontalDivider()
        }
    }
}

@Composable fun LoadedRelatedShowResults(
    relatedShowsResultsUiState: RelatedShowsResultsUiState.Results,
    modifier: Modifier = Modifier,
    onShowClick: (String) -> Unit = {},
) {
    Column(modifier = modifier) {
        for (show in relatedShowsResultsUiState.shows.sortedBy { it.artist }) {
            ShowListItem(
                artistName = show.artist,
                venueName = show.venueName,
                city = show.city,
                state = show.state,
                date = show.date,
                onClick = { onShowClick(show.id) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun ConfirmAddRemoveDialog(
    uiState: ConfirmationUiState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    saved: Boolean,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(216.dp)
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
                    "Confirm",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp, start = 24.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    if (saved)
                        "This show will be removed from your user profile. Do you wish to continue?"
                    else
                        "This show will be added to your user profile. Do you wish to continue?",
                    minLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                        onClick = { onConfirm() },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (uiState is ConfirmationUiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = LocalContentColor.current,
                                    strokeWidth = 1.5f.dp,
                                    strokeCap = StrokeCap.Round)
                            }
                            Text(
                                if (saved)
                                    "Remove"
                                else
                                    "Add",
                                modifier = Modifier.alpha(
                                    if (uiState is ConfirmationUiState.Loading) 0.0f
                                    else 1.0f
                                )
                            )
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
    val tracks = listOf(
        Track("Fury of the Storm", trackNumber = 1),
        Track("Cry Thunder", trackNumber = 2),
        Track("Power of the Triforce", trackNumber = 3),
        Track("The Last Dragonborn", trackNumber = 4),
        Track("Doomsday Party", trackNumber = 5),
        Track("My Heart Will Go On", trackNumber = 6, coverArtistName = "Celine Dion"),
    )
    val encoreTracks = listOf(
        Track("Through the Fire and Flames", trackNumber = 1)
    )
    val linkedShows = listOf(
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
            saved = true,
            linkedShows = linkedShows,
            tracks = tracks,
            encoreTracks = encoreTracks,
        ),
        relatedShowsResultsUiState = RelatedShowsResultsUiState.Hidden,
        confirmationUiState = ConfirmationUiState.NotLoading,
        onArtistClick = {},
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
        relatedShowsResultsUiState = RelatedShowsResultsUiState.Hidden,
        confirmationUiState = ConfirmationUiState.NotLoading,
        onArtistClick = {},
        onFindMoreClicked = {},
        onEditClicked = {},
        onToggleSaved = {},
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmAddDialogPreview() {
    ConfirmAddRemoveDialog(
        uiState = ConfirmationUiState.NotLoading,
        onDismissRequest = {},
        onConfirm = {},
        saved = false
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmRemoveDialogPreview() {
    ConfirmAddRemoveDialog(
        uiState = ConfirmationUiState.NotLoading,
        onDismissRequest = {},
        onConfirm = {},
        saved = true
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingConfirmDialogPreview() {
    ConfirmAddRemoveDialog(
        uiState = ConfirmationUiState.Loading,
        onDismissRequest = {},
        onConfirm = {},
        saved = false
    )
}
