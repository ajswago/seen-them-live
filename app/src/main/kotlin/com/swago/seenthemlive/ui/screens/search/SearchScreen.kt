package com.swago.seenthemlive.ui.screens.search

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.ui.components.cards.SearchCard
import com.swago.seenthemlive.ui.components.cards.SearchTerms
import com.swago.seenthemlive.ui.components.listitems.LoadingShowListItem
import com.swago.seenthemlive.ui.components.listitems.ShowListItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale

@Composable
fun SearchRoute(
    onBackClick: () -> Unit,
    onShowClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    SearchScreen(
        modifier = modifier,
        onSearch = { viewModel.performSearch() },
        uiState = viewModel.uiState,
        onShowClick = onShowClick,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onSearch: ((SearchTerms) -> Unit),
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onShowClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.search_title),
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
                SearchCard(
                    onSearch = { searchTerms -> onSearch(searchTerms) },
                    enabled = uiState !is SearchUiState.Loading,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.results_list_header),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .padding(start = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                when (uiState) {
                    SearchUiState.Empty -> {
                        Text(
                            "Perform a search to see results",
                            modifier = Modifier.padding(start = 24.dp)
                        )
                    }

                    SearchUiState.Loading -> {
                        LazyColumn {
                            items(3) {
                                LoadingShowListItem()
                                HorizontalDivider()
                            }
                        }
                    }

                    is SearchUiState.Results -> {
                        LazyColumn {
                            items(uiState.shows) { show ->
                                ShowListItem(
                                    artistName = show.artist,
                                    venueName = show.venueName,
                                    city = show.city,
                                    state = show.state,
                                    date = show.date,
                                    onClick = { onShowClick(show.id) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val shows = arrayOf(
        Show(
            id = "ID1",
            venueName = "Metlife Stadium",
            city = "East Rutherford",
            state = "NJ",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            tourName = "M72 World Tour",
            artist = "Metallica"
        ),
        Show(
            id = "ID2",
            venueName = "Metlife Stadium",
            city = "East Rutherford",
            state = "NJ",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-04") ?: Date(),
            tourName = "M72 World Tour",
            artist = "Metallica"
        ),
        Show(
            id = "ID3",
            venueName = "Metlife Stadium",
            city = "East Rutherford",
            state = "NJ",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-05-14") ?: Date(),
            tourName = "Worldwired Tour",
            artist = "Metallica"
        ),
    )
    val coroutineScope = rememberCoroutineScope()
    var uiState : SearchUiState by remember { mutableStateOf(SearchUiState.Empty) }
    SearchScreen(
        onSearch = {
            uiState = SearchUiState.Loading
            coroutineScope.launch {
                delay(Duration.ofMillis(2000))
                uiState = SearchUiState.Results(shows)
            }
        },
        uiState = uiState,
        onShowClick = {},
        modifier = Modifier
    )
}
