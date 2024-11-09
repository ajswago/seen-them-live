package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.formatForDisplay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GroupedShowHeader(
    venueName: String,
    locationName: String,
    date: Date,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandedChange: ((Boolean) -> Unit)
) {
    ListItem(
        headlineContent = { Text(venueName) },
        supportingContent = { Text(locationName) },
        leadingContent = {
            if (expanded) {
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.expanded_icon_description),
                )
            } else {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.collapsed_icon_description),
                )
            }
        },
        trailingContent = {
            Text(date.formatForDisplay()) },
        modifier = modifier.clickable(onClick = { onExpandedChange(!expanded) })
    )
}

@Preview(showBackground = true)
@Composable
fun GroupedShowHeaderPreview() {
    SeenThemLiveComposeTheme {
        var expanded by rememberSaveable { mutableStateOf(true) }
        GroupedShowHeader(
            venueName = "Capital One Hall",
            locationName = "Tysons Corner, VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-25") ?: Date(),
            expanded = expanded,
            onExpandedChange = { expanded = it}
        )
    }
}
