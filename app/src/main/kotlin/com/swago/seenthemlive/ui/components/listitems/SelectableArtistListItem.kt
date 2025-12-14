package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.shimmerLoading

@Composable
fun SelectableArtistListItem(
    artistName: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(artistName) },
        leadingContent = {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        },
        modifier = modifier.clickable(onClick = { onCheckedChange(!checked) })
    )
}

@Composable
fun LoadingSelectableArtistListItem(modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = { Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 12.dp)
                .width(200.dp)
                .height(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(2.dp))
                .shimmerLoading()
        ) },
        leadingContent = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 12.dp)
                    .size(24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SelectableArtistListItemPreview() {
    SeenThemLiveComposeTheme {
        var checked by rememberSaveable { mutableStateOf(true) }
        SelectableArtistListItem(
            artistName = "Rodrigo y Gabriela",
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingSelectableArtistListItemPreview() {
    SeenThemLiveComposeTheme {
        LoadingSelectableArtistListItem()
    }
}
