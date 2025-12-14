package com.swago.seenthemlive.ui.screens.profile

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.Profile
import com.swago.seenthemlive.ui.components.ListHeaderLabel
import com.swago.seenthemlive.ui.components.cards.LoadingProfileCard
import com.swago.seenthemlive.ui.components.cards.ProfileCard
import com.swago.seenthemlive.ui.components.listitems.ArtistListItem
import com.swago.seenthemlive.ui.components.listitems.LoadingArtistListItemDetailed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileRoute(
    onBackClick: () -> Unit,
    onArtistClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProfileScreen(
        uiState = uiState,
        onArtistClick = onArtistClick,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onArtistClick: (String) -> Unit = {},
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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
                    uiState = uiState
                )
                Spacer(modifier = Modifier.height(16.dp))
                ListHeaderLabel("Top Artists")
                Spacer(modifier = Modifier.height(8.dp))
                ArtistsList(
                    uiState = uiState,
                    onArtistClicked = onArtistClick
                )
            }
        }
    }
}

@Composable
fun ProfileCard(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ProfileUiState.Loading -> {
            LoadingProfileCard(modifier = modifier
                .padding(horizontal = 8.dp))
        }
        is ProfileUiState.Loaded -> {
            val profile = uiState.profile
            ProfileCard(
                profileName = profile.name,
                email = profile.email,
                showCount = profile.showCount,
                artistCount = profile.artistCount,
                venueCount = profile.venueCount,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ArtistsList(
    uiState: ProfileUiState,
    onArtistClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ProfileUiState.Loading -> {
            LoadingArtistsList(modifier = modifier)
        }
        is ProfileUiState.Loaded -> {
            LoadedArtistsList(
                uiState = uiState,
                onArtistClicked = onArtistClicked,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LoadingArtistsList(
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(4) {
            LoadingArtistListItemDetailed()
            HorizontalDivider()
        }
    }
}

@Composable
fun LoadedArtistsList(
    uiState: ProfileUiState.Loaded,
    modifier: Modifier = Modifier,
    onArtistClicked: (String) -> Unit = {},
) {
    val artists = uiState.artists
    LazyColumn(modifier = modifier) {
        items(artists) { artist ->
            ArtistListItem(
                artistName = artist.name,
                lastShow = artist.lastShow,
                showCount = artist.showCount,
                showAvatar = true,
                onClick = { onArtistClicked(artist.id) }
            )
            HorizontalDivider()
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
    val profile = Profile(
        name = "Anthony Swago",
        email = "ajswago@gmail.com",
        showCount = 72,
        venueCount = 34,
        artistCount = 78,
    )
    ProfileScreen(
        uiState = ProfileUiState.Loaded(
            profile = profile,
            artists = artists
        ),
        onArtistClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingProfileScreenPreview() {
    ProfileScreen(
        uiState = ProfileUiState.Loading
    )
}
