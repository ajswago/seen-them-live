package com.swago.seenthemlive.ui.screens.shows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.ui.components.AppBarWithProfile
import com.swago.seenthemlive.ui.components.ProfileMenuItem
import com.swago.seenthemlive.ui.components.listitems.ExpandableShowListGroup
import com.swago.seenthemlive.ui.components.listitems.LoadingExpandableShowListGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShowsListRoute(
    onProfileMenuOption: (ProfileMenuItem) -> Unit,
    onAddClick: () -> Unit,
    onShowClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ShowsListScreen(
        uiState = uiState,
        onProfileMenuOption = onProfileMenuOption,
        onAddClick = onAddClick,
        onShowClick = onShowClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowsListScreen(
    uiState: ShowsListUiState,
    modifier: Modifier = Modifier,
    onProfileMenuOption: (ProfileMenuItem) -> Unit = {},
    onAddClick: () -> Unit = {},
    onShowClick: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppBarWithProfile(
                stringResource(R.string.shows_list_header),
                enabled = uiState !is ShowsListUiState.Loading,
                onProfileMenuOption = onProfileMenuOption
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
            ShowsList(
                uiState = uiState,
                onShowClick = onShowClick
            )
            if (uiState !is ShowsListUiState.Loading) {
                FloatingActionButton(
                    onClick = { onAddClick() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add"
                    )
                }
            }
        }
    }
}

@Composable
fun ShowsList(
    uiState: ShowsListUiState,
    onShowClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        ShowsListUiState.Loading -> {
            LoadingShowsList(modifier = modifier)
        }
        is ShowsListUiState.Loaded -> {
            LoadedShowsList(
                uiState = uiState,
                onShowClick = onShowClick,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LoadingShowsList(modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(3) {
            LoadingExpandableShowListGroup()
        }
    }
}

@Composable
fun LoadedShowsList(
    uiState: ShowsListUiState.Loaded,
    onShowClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedShows = uiState.shows.groupBy { it.date }
    LazyColumn(modifier = modifier) {
        items(groupedShows.keys.toTypedArray().sortedDescending()) { date ->
            groupedShows[date]?.let { show ->
                val artists = show.map { it.artist }.toTypedArray()
                ExpandableShowListGroup(
                    venueName = show.first().venueName,
                    city = show.first().city,
                    state = show.first().state,
                    date = date,
                    artistList = artists,
                    onArtistClick = { index -> onShowClick(show[index].id) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowsListScreenPreview() {
    val shows = listOf(
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
        uiState = ShowsListUiState.Loaded(shows),
        onProfileMenuOption = { print("Option clicked: $it") },
        onShowClick = { print("Show clicked: $it") },
        onAddClick = {},
        modifier = Modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingShowsListScreenPreview() {
    ShowsListScreen(ShowsListUiState.Loading)
}
