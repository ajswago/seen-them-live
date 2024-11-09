package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.formatCommaSeparatedString
import com.swago.seenthemlive.util.formatForDisplay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GroupedShowListItem(
    venueName: String,
    locationName: String,
    artistList: Array<String>,
    date: Date,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(venueName) },
        supportingContent = {
            Column {
                Text(
                    locationName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    artistList.formatCommaSeparatedString(2),
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
                Text(
                    date.formatForDisplay(),
                    modifier = Modifier.padding(top = 4.dp))
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
fun GroupedShowListItemPreview() {
    SeenThemLiveComposeTheme {
        GroupedShowListItem(
            venueName = "PPL Center",
            locationName = "Allentown, Pennsylvania",
            artistList = arrayOf("Megadeth", "Trivium"),
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupedShowListItemPreview2() {
    SeenThemLiveComposeTheme {
        GroupedShowListItem(
            venueName = "Jiffy Lube Live",
            locationName = "Bristow, Virginia",
            artistList = arrayOf("Anthrax", "Behemoth", "Slayer", "Lamb of God"),
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date(),
            onClick = {}
        )
    }
}
