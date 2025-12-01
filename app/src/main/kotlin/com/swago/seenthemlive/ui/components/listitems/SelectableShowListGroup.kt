package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
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
    city: String,
    state: String,
    date: Date,
    artistList: Array<String>,
    selections: Map<String, Boolean>,
    onArtistSelected: ((Int, Boolean) -> Unit),
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
    ) {
        ExpandableShowHeader(
            venueName = venueName,
            city = city,
            state = state,
            date = date,
            expanded = expanded,
            onExpandedChange = { expanded = it }
        )
        HorizontalDivider()
        if (expanded) {
            artistList.forEachIndexed { index, artist ->
                SelectableArtistListItem(
                    artistName = artist,
                    checked = selections[artist] ?: false,
                    onCheckedChange = { onArtistSelected(index, it) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectableShowListGroupPreview() {
    val artists = arrayOf("Dethklok", "DragonForce", "Nekrogoblikon")
    val artistSelections = remember {
        mutableStateMapOf(*artists.map { it to false }.toTypedArray())
    }
//    val artists = remember {
//        mutableListOf("Dethklok", "DragonForce", "Nekrogoblikon")
//    }
    SelectableShowListGroup(
        venueName = "The Fillmore Silver Spring",
        city = "Silver Spring",
        state = "MD",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        artistList = artists,
        selections = artistSelections,
        onArtistSelected = { index, selected ->
            artistSelections[artists[index]] = selected
        }
    )
}
