package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
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
fun ExpandableShowHeader(
    venueName: String,
    city: String,
    state: String,
    date: Date,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandedChange: ((Boolean) -> Unit)
) {
    ListItem(
        headlineContent = { Text(venueName) },
        supportingContent = { Text(stringResource(R.string.location_format, city, state)) },
        leadingContent = {
            if (expanded) {
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.expanded_icon_description),
                )
            } else {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = stringResource(R.string.collapsed_icon_description),
                )
            }
        },
        trailingContent = {
            Text(date.formatForDisplay()) },
        modifier = modifier.clickable(onClick = { onExpandedChange(!expanded) })
    )
}

@Composable
fun LoadingExpandableShowHeader() {
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
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(R.string.expanded_icon_description),
            )
        },
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
fun ExpandableShowHeaderPreview() {
    SeenThemLiveComposeTheme {
        var expanded by rememberSaveable { mutableStateOf(true) }
        ExpandableShowHeader(
            venueName = "Capital One Hall",
            city = "Tysons Corner",
            state = "VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-25") ?: Date(),
            expanded = expanded,
            onExpandedChange = { expanded = it}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingExpandableShowHeaderPreview() {
    SeenThemLiveComposeTheme {
        LoadingExpandableShowHeader()
    }
}
