package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme

@Composable
fun FindMoreListItem(
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val alpha = if (enabled) 1.0f else 0.5f
    ListItem(
        headlineContent = {
            Text(
                stringResource(R.string.find_more_headline),
                modifier = Modifier.alpha(alpha)
            )
        },
        leadingContent = {
            Icon(
                Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_icon_description),
                modifier = Modifier.alpha(alpha)
            )
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = stringResource(R.string.more_content_icon_description),
                modifier = Modifier.alpha(alpha)
            )
        },
        modifier = modifier.clickable(onClick = { onClick() })
    )
}

@Preview(showBackground = true)
@Composable
fun FindMoreListItemPreview() {
    SeenThemLiveComposeTheme {
        FindMoreListItem(
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FindMoreListItemDiabledPreview() {
    SeenThemLiveComposeTheme {
        FindMoreListItem(
            onClick = {},
            enabled = false
        )
    }
}
