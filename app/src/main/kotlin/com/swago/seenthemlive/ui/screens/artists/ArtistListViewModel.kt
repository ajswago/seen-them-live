package com.swago.seenthemlive.ui.screens.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.models.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.time.delay
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel @Inject constructor() : ViewModel() {

    val artistsFlow: Flow<Array<Artist>> = flow {
        delay(Duration.ofMillis(2000))
        emit(artists)
    }

    val uiState: StateFlow<ArtistListUiState> =
        artistsFlow.map {
            ArtistListUiState.Loaded(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = ArtistListUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val artists = arrayOf(
        Artist(
            id = "ID1",
            name = "AC/DC",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2016-09-17") ?: Date(),
            showCount = 1
        ),
        Artist(
            id = "ID2",
            name = "Children of Bodom",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2014-03-21") ?: Date(),
            showCount = 3
        ),
        Artist(
            id = "ID3",
            name = "Five Finger Death Punch",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            showCount = 2
        ),
        Artist(
            id = "ID4",
            name = "Testament",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2018-06-10") ?: Date(),
            showCount = 3
        )
    )
}

sealed interface ArtistListUiState {
    data object Loading : ArtistListUiState
    data class Loaded(
        val artists: Array<Artist>
    ) : ArtistListUiState
}
