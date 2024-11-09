package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CollapsibleShowListGroup(
    venueName: String,
    locationName: String,
    date: Date,
    artistList: Array<String>,
    modifier: Modifier = Modifier,
    onArtistClick: ((String) -> Unit)
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
            artistList.forEach {
                ArtistListItem(
                    artistName = it,
                    onClick = { onArtistClick(it) },
                    modifier = Modifier
                        .height(50.dp)
                )
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollapsibleShowListGroupPreview() {
    CollapsibleShowListGroup(
        venueName = "Capital One Hall",
        locationName = "Tysons Corner, Virginia",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-25") ?: Date(),
        artistList = arrayOf("Rodrigo y Gabriela"),
        onArtistClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CollapsibleShowListGroupMultiplePreview() {
    CollapsibleShowListGroup(
        venueName = "The Fillmore Silver Spring",
        locationName = "Silver Spring, Maryland",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        artistList = arrayOf("Dethklok", "DragonForce", "Nekrogoblikon"),
        onArtistClick = {}
    )
}
