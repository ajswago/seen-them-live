package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.formatForDisplay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShowListItem(
    artistName: String,
    venueName: String,
    locationName: String,
    date: Date,
    modifier: Modifier = Modifier,
    tourName: String? = null,
    onClick: (() -> Unit)
) {
    ListItem(
        headlineContent = { Text(artistName) },
        supportingContent = {
            Column {
                Text(
                    venueName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    locationName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }},
        trailingContent = {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .height(64.dp)
                    .padding(top = 2.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(date.formatForDisplay())
                    tourName?.let { tourName ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            tourName,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .widthIn(max = 86.dp)
                        )
                    }
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = stringResource(R.string.more_content_icon_description),
                )
            }
        },
        modifier = modifier.clickable(onClick = { onClick() })
    )
}

@Preview(showBackground = true)
@Composable
fun ShowListItemPreview() {
    SeenThemLiveComposeTheme {
        ShowListItem(
            artistName = "DragonForce",
            venueName = "Revolution Concert House & Event Center",
            locationName = "Garden City, Idaho",
            tourName = "Warp Speed Warriors",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-05-02") ?: Date(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShowListItemPreview2() {
    SeenThemLiveComposeTheme {
        ShowListItem(
            artistName = "Metallica",
            venueName = "MetLife Stadium",
            locationName = "East Rutherford, New Jersey",
            tourName = "M72 World Tour",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShowListItemMissingTourPreview() {
    SeenThemLiveComposeTheme {
        ShowListItem(
            artistName = "Metallica",
            venueName = "MetLife Stadium",
            locationName = "East Rutherford, New Jersey",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            onClick = {}
        )
    }
}
