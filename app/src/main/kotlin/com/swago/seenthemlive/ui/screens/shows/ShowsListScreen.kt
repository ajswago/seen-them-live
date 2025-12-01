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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowsListViewModel = hiltViewModel()
) {
    ShowsListScreen(
        shows = viewModel.shows,
        onProfileMenuOption = {},
        onShowClicked = {},
        onAddClick = onAddClick,
        modifier = modifier,
        loading = viewModel.loading,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowsListScreen(
    shows: Array<Show>,
    onProfileMenuOption: (ProfileMenuItem) -> Unit,
    onShowClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    loading: Boolean = false
) {
    val groupedShows = shows.groupBy { it.date }
    Scaffold(
        topBar = {
            AppBarWithProfile(
                stringResource(R.string.shows_list_header),
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
            if (loading) {
                LazyColumn {
                    items(3) {
                        LoadingExpandableShowListGroup()
                    }
                }
            } else {
                LazyColumn {
                    items(groupedShows.keys.toTypedArray()) { date ->
                        groupedShows[date]?.let { show ->
                            val artists = show.map { it.artist }.toTypedArray()
                            ExpandableShowListGroup(
                                venueName = show.first().venueName,
                                city = show.first().city,
                                state = show.first().state,
                                date = date,
                                artistList = artists,
                                onArtistClick = { index -> onShowClicked(show[index].id) }
                            )
                        }
                    }
                }
            }
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

@Preview(showBackground = true)
@Composable
fun ShowsListScreenPreview() {
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
        onProfileMenuOption = { print("Option clicked: $it") },
        onShowClicked = { print("Show clicked: $it") },
        onAddClick = {},
        modifier = Modifier,
        loading = false
    )
}
