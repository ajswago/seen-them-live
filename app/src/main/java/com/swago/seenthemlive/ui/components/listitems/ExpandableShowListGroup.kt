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
fun ExpandableShowListGroup(
    venueName: String,
    city: String,
    state: String,
    date: Date,
    artistList: Array<String>,
    modifier: Modifier = Modifier,
    onArtistClick: ((Int) -> Unit)? = null
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
        Divider()
        if (expanded) {
            artistList.forEachIndexed { index, name ->
                ArtistListItem(
                    artistName = name,
                    indent = true,
                    onClick = { onArtistClick?.invoke(index) },
                    modifier = Modifier
                        .height(50.dp)
                )
                Divider()
            }
        }
    }
}

@Composable
fun LoadingExpandableShowListGroup() {
    LoadingExpandableShowHeader()
    Divider()
    LoadingArtistListItemSimple()
    Divider()
    LoadingArtistListItemSimple()
    Divider()
}

@Preview(showBackground = true)
@Composable
fun ExpandableShowListGroupPreview() {
    ExpandableShowListGroup(
        venueName = "Capital One Hall",
        city = "Tysons Corner",
        state = "VA",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-25") ?: Date(),
        artistList = arrayOf("Rodrigo y Gabriela"),
        onArtistClick = {
            println("Index tapped: $it")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExpandableShowListGroupMultiplePreview() {
    ExpandableShowListGroup(
        venueName = "The Fillmore Silver Spring",
        city = "Silver Spring",
        state = "MD",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        artistList = arrayOf("Dethklok", "DragonForce", "Nekrogoblikon"),
        onArtistClick = {
            println("Index tapped: $it")
        }
    )
}
