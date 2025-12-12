package com.swago.seenthemlive.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme

@Composable
fun SearchCard(
    onSearch: ((SearchTerms) -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var artist by remember { mutableStateOf<String?>(null) }
    var venue by remember { mutableStateOf<String?>(null) }
    var usState by remember { mutableStateOf(usStates[0]) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.search_shows_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                enabled = enabled,
                value = artist ?: "",
                onValueChange = { artist = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(stringResource(R.string.artist_field_label))
                },
                trailingIcon = {
                    Icon(
                        Icons.Outlined.Cancel,
                        contentDescription = stringResource(R.string.cancel_button_description),
                        modifier = Modifier
                            .clickable { artist = null }
                    )
                }
            )
            OutlinedTextField(
                enabled = enabled,
                value = venue ?: "",
                onValueChange = { venue = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(stringResource(R.string.venue_field_label))
                },
                trailingIcon = {
                    Icon(
                        Icons.Outlined.Cancel,
                        contentDescription = stringResource(R.string.cancel_button_description),
                        modifier = Modifier
                            .clickable { venue = null }
                    )
                }
            )
            UsStateSelectionDropdownMenu(
                enabled = enabled,
                selectedText = usState,
                onSelectionChange = { usState = it }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                enabled = enabled,
                onClick = { onSearch(
                    SearchTerms(
                        artist = artist,
                        venue = venue,
                        usState = usState
                    )
                ) },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text(stringResource(R.string.search_button_label))
            }
        }
    }
}

data class SearchTerms(
    var artist: String?,
    var venue: String?,
    var usState: String?,
)

@Preview(showBackground = true)
@Composable
fun SearchCardPreview() {
    SeenThemLiveComposeTheme {
        SearchCard(onSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchCardDisabledPreview() {
    SeenThemLiveComposeTheme {
        SearchCard(onSearch = {}, enabled = false)
    }
}
