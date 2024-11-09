package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SelectableShowListGroup(
    venueName: String,
    locationName: String,
    date: Date,
    artistList: Map<String, Boolean>,
    modifier: Modifier = Modifier,
    onArtistSelected: ((String, Boolean) -> Unit)
) {
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
    ) {
        GroupedShowHeader(
            venueName = venueName,
            locationName = locationName,
            date = date,
            expanded = expanded,
            onExpandedChange = { expanded = it }
        )
        Divider()
        if (expanded) {
            artistList.forEach { artistSelection ->
                SelectableArtistListItem(
                    artistName = artistSelection.key,
                    checked = artistSelection.value,
                    onCheckedChange = { onArtistSelected(artistSelection.key, it) }
                )
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectableShowListGroupPreview() {
    val artistSelections = remember {
        mutableStateMapOf("Dethklok" to false, "DragonForce" to false, "Nekrogoblikon" to false)
    }
    SelectableShowListGroup(
        venueName = "The Fillmore Silver Spring",
        locationName = "Silver Spring, Maryland",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        artistList = artistSelections,
        onArtistSelected = { artist, selected ->
            artistSelections[artist] = selected
        }
    )
}
