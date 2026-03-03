package com.swago.seenthemlive.ui.screens.artists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun ArtistSortSelector(
    onArtistSortChanged: (ArtistSort) -> Unit,
    onArtistSortOrderChanged: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
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
                        onArtistSortChanged(sort)
                    },
                    selected = index == selectedIndex,
                    label = { Text(sort.label) },
                    enabled = enabled
                )
            }
        }
        IconButton(
            onClick = onArtistSortOrderChanged,
            enabled = enabled,
            modifier = Modifier.align(Alignment.CenterEnd)) {
            Icon(
                imageVector = Icons.Outlined.SwapVert,
                contentDescription = "Sort Direction"
            )
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
fun ArtistSortSelectorPreview() {
    var artistSort by remember { mutableStateOf(ArtistSort.NAME) }
    var sortAscending by remember { mutableStateOf(true) }
    ArtistSortSelector(
        onArtistSortChanged = {
            artistSort = it
            sortAscending = it.defaultAscending
        },
        onArtistSortOrderChanged = { sortAscending = !sortAscending }
    )
}

@Preview(showBackground = true)
@Composable
fun ArtistSortSelectorDisabledPreview() {
    ArtistSortSelector(
        onArtistSortChanged = {},
        onArtistSortOrderChanged = {},
        enabled = false
    )
}
