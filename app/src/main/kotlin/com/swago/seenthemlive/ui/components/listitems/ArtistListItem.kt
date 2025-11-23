package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.components.TextAvatar
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.formatForDisplay
import com.swago.seenthemlive.util.shimmerLoading
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
    indent: Boolean? = false,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        headlineContent = {
            Text(
                artistName,
                modifier = Modifier
                    .padding(start = if (indent == true) 24.dp else 0.dp)
            )
        },
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                showCount?.let { count ->
                    Text(count.toString())
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = stringResource(R.string.more_content_icon_description),
                )
            }
        },
        modifier =
            modifier
                .clickable(onClick = { onClick?.invoke() })
    )
}

@Composable
fun LoadingArtistListItemDetailed() {
    ListItem(
        headlineContent = { Box(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .width(200.dp)
                .height(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(2.dp))
                .shimmerLoading()
        ) },
        supportingContent = { Box(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .width(200.dp)
                .height(12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(2.dp))
                .shimmerLoading()
        ) },
        leadingContent = {
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .size(40.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .shimmerLoading()
            )
        },
        trailingContent = {
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(end = 20.dp)
                    .width(20.dp)
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
        },
    )
}

@Composable
fun LoadingArtistListItemSimple() {
    ListItem(
        headlineContent = { Box(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .padding(start = 24.dp)
                .width(200.dp)
                .height(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(2.dp))
                .shimmerLoading()
        ) }
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
fun ArtistListItemIndentedPreview() {
    SeenThemLiveComposeTheme {
        ArtistListItem(
            artistName = "Rodrigo y Gabriela",
            indent = true,
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
            showAvatar = true,
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

@Preview(showBackground = true)
@Composable
fun ArtistListItemLoadingSimplePreview() {
    SeenThemLiveComposeTheme {
        LoadingArtistListItemSimple()
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistListItemLoadingDetailedPreview() {
    SeenThemLiveComposeTheme {
        LoadingArtistListItemDetailed()
    }
}
