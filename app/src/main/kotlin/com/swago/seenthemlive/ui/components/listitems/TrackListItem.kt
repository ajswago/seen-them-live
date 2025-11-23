package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme

@Composable
fun TrackListItem(
    trackName: String,
    modifier: Modifier = Modifier,
    trackNumber: Int? = null,
    trackCount: Int? = null,
    coverArtistName: String? = null,
    isTapeTrack: Boolean = false
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    ListItem(
        headlineContent = { Text(trackName) },
        supportingContent = {
            coverArtistName?.let { cover ->
                Text(String.format(
                    stringResource(R.string.cover_artist_format),
                    cover))
            }},
        leadingContent = {
            trackNumber?.let { trackNumber ->
                Text(String.format(
                    stringResource(R.string.track_number_format),
                    trackNumber))
            }
        },
        trailingContent = {
            trackCount?.let { count ->
                Text(count.toString())
            }
        },
        colors = ListItemDefaults.colors(
            headlineColor = if (isTapeTrack) textColor.copy(alpha = 0.38f) else textColor,
            supportingColor = if (isTapeTrack) textColor.copy(alpha = 0.38f) else textColor,
            leadingIconColor = if (isTapeTrack) textColor.copy(alpha = 0.38f) else textColor
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TrackListItemCoverPreview() {
    SeenThemLiveComposeTheme {
        TrackListItem(
            trackName = "It's a Long Way to the Top (If You Wanna Rock 'n' Roll)",
            coverArtistName = "AC/DC",
            isTapeTrack = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TrackListItemPreview() {
    SeenThemLiveComposeTheme {
        TrackListItem(
            trackName = "Fury of the Storm",
            trackNumber = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TrackListItemCountPreview() {
    SeenThemLiveComposeTheme {
        TrackListItem(
            trackName = "Now You've Got Something to Die For",
            trackCount = 5
        )
    }
}
