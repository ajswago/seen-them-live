package com.swago.seenthemlive.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme

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
