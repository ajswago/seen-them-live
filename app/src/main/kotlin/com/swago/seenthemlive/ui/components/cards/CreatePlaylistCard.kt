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
fun CreatePlaylistCard(
    onCreate: ((String?) -> Unit),
    modifier: Modifier = Modifier
) {
    var playlistName by remember { mutableStateOf<String?>(null) }
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
                text = stringResource(R.string.create_playlist_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = playlistName ?: "",
                onValueChange = { playlistName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(stringResource(R.string.playlist_name_field_label))
                },
                trailingIcon = {
                    Icon(
                        Icons.Outlined.Cancel,
                        contentDescription = stringResource(R.string.cancel_button_description),
                        modifier = Modifier
                            .clickable { playlistName = null }
                    )
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onCreate(playlistName) },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text(stringResource(R.string.create_button_label))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePlaylistCardPreview() {
    SeenThemLiveComposeTheme {
        CreatePlaylistCard(onCreate = {})
    }
}
