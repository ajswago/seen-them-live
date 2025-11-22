package com.swago.seenthemlive.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.ui.components.cards.ProfileCard
import com.swago.seenthemlive.ui.components.listitems.ArtistListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileName: String,
    email: String,
    showCount: Int,
    artistCount: Int,
    venueCount: Int,
    topArtists: Array<Artist>,
    onArtistClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column {
                ProfileCard(
                    profileName = profileName,
                    email = email,
                    showCount = showCount,
                    artistCount = artistCount,
                    venueCount = venueCount,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Top Artists",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .padding(start = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(topArtists) { artist ->
                        ArtistListItem(
                            artistName = artist.name,
                            lastShow = artist.lastShow,
                            showCount = artist.showCount,
                            showAvatar = true,
                            onClick = { onArtistClicked(artist.id) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val artists = arrayOf(
        Artist(
            "ID1",
            name= "Lamb of God",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID2",
            name= "Megadeth",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID3",
            name= "Opeth",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID4",
            name= "Iron Maiden",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 4
        )
    )
    ProfileScreen(
        profileName = "Anthony Swago",
        email = "ajswago@gmail.com",
        showCount = 72,
        venueCount = 34,
        artistCount = 78,
        topArtists = artists,
        onArtistClicked = {}
    )
}
