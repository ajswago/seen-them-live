package com.swago.seenthemlive.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.ui.components.listitems.CollapsibleShowListGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowsListScreen(
    shows: Array<GroupedShow>,
    onProfileButton: () -> Unit,
    onShowClicked: (GroupedShow) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.shows_list_header),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { onProfileButton() }) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = stringResource(R.string.account_button_description)
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
            LazyColumn {
                items(shows) { show ->
                    CollapsibleShowListGroup(
                        venueName = show.venueName,
                        locationName = show.locationName,
                        date = show.date,
                        artistList = show.artistList,
                        onArtistClick = { onShowClicked(show) }
                    )
                }
            }
            FloatingActionButton(
                onClick = {},
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
        GroupedShow(
            venueName = "Capital One Hall",
            locationName = "Tysons Corner, Virginia",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date(),
            artistList = arrayOf("Rodrigo y Gabriela")
        ),
        GroupedShow(
            venueName = "Warner Theatre",
            locationName = "Washington, Washington, D.C.",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-11") ?: Date(),
            artistList = arrayOf("Joe Satriani", "Steve Vai")
        ),
        GroupedShow(
            venueName = "The Fillmore Silver Spring",
            locationName = "Silver Spring, Maryland",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-09") ?: Date(),
            artistList = arrayOf("Dethklok", "DragonForce", "Nekrogoblikon")
        ),
    )
    ShowsListScreen(
        shows = shows,
        onProfileButton = {},
        onShowClicked = {},
        modifier = Modifier
    )
}
