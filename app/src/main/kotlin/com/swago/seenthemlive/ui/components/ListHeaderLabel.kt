package com.swago.seenthemlive.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R

@Composable
fun ListHeaderLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(start = 24.dp)
    )
}

@Composable
fun ListHeaderLabel(@StringRes id: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(start = 24.dp)
    )
}

@Preview
@Composable
fun ListHeaderLabelPreview() {
    ListHeaderLabel("Setlist")
}

@Preview
@Composable
fun ListHeaderLabelResourcePreview() {
    ListHeaderLabel(R.string.shows_list_header)
}
