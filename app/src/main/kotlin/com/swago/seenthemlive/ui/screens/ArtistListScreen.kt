package com.swago.seenthemlive.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.ui.components.AppBarWithProfile
import com.swago.seenthemlive.ui.components.ProfileMenuItem
import com.swago.seenthemlive.ui.components.listitems.ArtistListItem
import com.swago.seenthemlive.ui.components.listitems.LoadingArtistListItemDetailed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListScreen(
    artists: Array<Artist>,
    onProfileMenuOption: (ProfileMenuItem) -> Unit,
    onArtistClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false
) {
    Scaffold(
        topBar = {
            AppBarWithProfile(
                "Artists",
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
            var artistSort by remember { mutableStateOf(ArtistSort.NAME) }
            var sortAscending by remember { mutableStateOf(true) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    var selectedIndex by remember { mutableIntStateOf(0) }
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        ArtistSort.entries.forEachIndexed { index, sort ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = ArtistSort.entries.count()
                                ),
                                onClick = {
                                    selectedIndex = index
                                    artistSort = sort
                                    sortAscending = sort.defaultAscending
                                },
                                selected = index == selectedIndex,
                                label = { Text(sort.label) }
                            )
                        }
                    }
                    IconButton(onClick = { sortAscending = !sortAscending }, modifier = Modifier.align(Alignment.CenterEnd)) {
                        Icon(
                            imageVector = Icons.Outlined.SwapVert,
                            contentDescription = "Sort Direction"
                        )
                    }
                }
                if (loading) {
                    LazyColumn {
                        items(3) {
                            LoadingArtistListItemDetailed()
                        }
                    }
                } else {
                    val artistsSorted = when (artistSort) {
                        ArtistSort.NAME ->
                            if (sortAscending) artists.sortedBy { it.name }
                            else artists.sortedByDescending { it.name }
                        ArtistSort.RECENT ->
                            if (sortAscending) artists.sortedBy { it.lastShow }
                            else artists.sortedByDescending { it.lastShow }
                        ArtistSort.SHOW_COUNT ->
                            if (sortAscending) artists.sortedBy { it.showCount }
                            else artists.sortedByDescending { it.showCount }
                        }
                    LazyColumn {
                        items(artistsSorted) { artist ->
                            ArtistListItem(
                                artist.name,
                                lastShow = artist.lastShow,
                                showAvatar = true,
                                showCount = artist.showCount,
                                onClick = { onArtistClicked(artist.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class ArtistSort(var label: String, var defaultAscending: Boolean) {
    NAME("Name", true),
    RECENT("Recent", false),
    SHOW_COUNT("Shows", false)
}

@Preview(showBackground = true)
@Composable
fun ArtistListScreenPreview() {
    val artists = arrayOf(
        Artist(
            id = "ID1",
            name = "AC/DC",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2016-09-17") ?: Date(),
            showCount = 1
        ),
        Artist(
            id = "ID2",
            name = "Children of Bodom",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2014-03-21") ?: Date(),
            showCount = 3
        ),
        Artist(
            id = "ID3",
            name = "Five Finger Death Punch",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            showCount = 2
        ),
        Artist(
            id = "ID4",
            name = "Testament",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2018-06-10") ?: Date(),
            showCount = 3
        )
    )
    ArtistListScreen(
        artists = artists,
        onProfileMenuOption = {},
        onArtistClicked = {},
        loading = false
    )
}
