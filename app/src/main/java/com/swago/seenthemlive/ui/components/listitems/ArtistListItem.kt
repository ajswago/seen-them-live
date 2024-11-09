package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.components.TextAvatar
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.formatForDisplay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ArtistListItem(
    artistName: String,
    modifier: Modifier = Modifier,
    lastShow: Date? = null,
    showCount: Int? = null,
    showAvatar: Boolean = false,
    onClick: (() -> Unit)
) {
    ListItem(
        headlineContent = { Text(artistName) },
        supportingContent = {
            lastShow?.let { date ->
                Text(String.format(
                    stringResource(R.string.last_show_date_format),
                    date.formatForDisplay()))
            }},
        leadingContent = {
            if (showAvatar) {
                TextAvatar(artistName.first())
            }
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                showCount?.let { count ->
                    Text(count.toString())
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
fun ArtistListItemPreview() {
    SeenThemLiveComposeTheme {
        ArtistListItem(
            artistName = "Rodrigo y Gabriela",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistListItemCountPreview() {
    SeenThemLiveComposeTheme {
        ArtistListItem(
            artistName = "Opeth",
            showCount = 5,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistListItemAvatarPreview() {
    SeenThemLiveComposeTheme {
        ArtistListItem(
            artistName = "AC/DC",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2016-09-17") ?: Date(),
            showCount = 1,
            showAvatar = true,
            onClick = {}
        )
    }
}
