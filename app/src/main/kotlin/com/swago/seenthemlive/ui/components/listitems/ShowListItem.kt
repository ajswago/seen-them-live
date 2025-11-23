package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.formatForDisplay
import com.swago.seenthemlive.util.shimmerLoading
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShowListItem(
    artistName: String,
    venueName: String,
    city: String,
    state: String,
    date: Date,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        overlineContent = { Text(stringResource(R.string.location_format, city, state)) },
        headlineContent = { Text(artistName) },
        supportingContent = {
            Text(
                venueName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(date.formatForDisplay())
                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = stringResource(R.string.more_content_icon_description),
                )
            }
        },
        modifier = modifier.clickable(onClick = { onClick?.invoke() })
    )
}

@Composable
fun LoadingShowListItem() {
    ListItem(
        overlineContent = { Box(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .width(200.dp)
                .height(12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(2.dp))
                .shimmerLoading()
        ) },
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
        trailingContent = {
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(end = 20.dp)
                    .width(60.dp)
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ShowListItemPreview() {
    SeenThemLiveComposeTheme {
        ShowListItem(
            artistName = "DragonForce",
            venueName = "Revolution Concert House & Event Center",
            city = "Garden City",
            state = "ID",
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
            city = "East Rutherford",
            state = "NJ",
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
            city = "East Rutherford",
            state = "NJ",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingShowListItemPreview() {
    SeenThemLiveComposeTheme {
        LoadingShowListItem()
    }
}
