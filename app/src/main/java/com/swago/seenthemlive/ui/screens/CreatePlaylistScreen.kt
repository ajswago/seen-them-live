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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.ui.components.cards.CreatePlaylistCard
import com.swago.seenthemlive.ui.components.listitems.CollapsibleShowListGroup
import com.swago.seenthemlive.ui.components.listitems.SelectableShowListGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(
    shows: Array<GroupedShow>,
    onCreatePlaylist: (String, Array<Show>) -> Unit,
    modifier: Modifier = Modifier
) {
    val artistSelections = remember {
        mutableStateMapOf(*shows.map { it to it.artistList.associateBy({ artist -> artist}, {false}).toMutableMap() }.toTypedArray())
//        shows.associateBy({it}, { show ->
//            show.artistList.associateBy({ artist -> artist}, {false}).toMutableMap()
//        })
//        show.artistList.associateBy({it}, {false}),
//        mutableStateMapOf("Dethklok" to false, "DragonForce" to false, "Nekrogoblikon" to false)
    }
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
                CreatePlaylistCard(
                    onCreate = { name ->
                        name?.let {
                            onCreatePlaylist(it, arrayOf())
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.shows_list_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(shows) { show ->
                        artistSelections[show]?.let {
                            SelectableShowListGroup(
                                venueName = show.venueName,
                                locationName = show.locationName,
                                date = show.date,
                                artistList = it,
                                onArtistSelected = { artist, selected ->
                                    it[artist] = selected
                                    artistSelections[show] = it
//                                    it[artist] = selected
                                }
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
fun CreatePlaylistScreenPreview() {
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
    CreatePlaylistScreen(
        shows = shows,
        onCreatePlaylist = { name, shows -> }
    )
}
