package com.swago.seenthemlive.ui.screens.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.time.delay
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale

@HiltViewModel(assistedFactory = ShowViewModel.Factory::class)
class ShowViewModel @AssistedInject constructor(
    @Assisted val showId: String,
) : ViewModel() {

    val showFlow: Flow<Show> = flow {
        delay(Duration.ofMillis(2000))
        emit(show)
    }

    val tracksFlow: Flow<Array<Track>> = flow {
        delay(Duration.ofMillis(2000))
        emit(tracks)
    }

    val linkedShowsFlow: Flow<Array<Show>> = flow {
        delay(Duration.ofMillis(2000))
        emit(linkedShows)
    }

    val uiState: StateFlow<ShowUiState> = combine(
        showFlow,
        tracksFlow,
        linkedShowsFlow,
        ShowUiState::Loaded
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShowUiState.Loading
    )

    val show = Show(
        id = "ID1",
        venueName = "The Fillmore Silver Spring",
        city = "Silver Spring",
        state = "MD",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        tourName = "Warp Speed Warriors",
        artist = "DragonForce"
    )
    val tracks = arrayOf(
        Track("Fury of the Storm", trackNumber = 1),
        Track("Cry Thunder", trackNumber = 2),
        Track("Power of the Triforce", trackNumber = 3),
        Track("The Last Dragonborn", trackNumber = 4),
        Track("Doomsday Party", trackNumber = 5),
        Track("My Heart Will Go On", trackNumber = 6, coverArtistName = "Celine Dion"),
        Track("Through the Fire and Flames", trackNumber = 7)
    )
    val linkedShows = arrayOf(
        Show(
            id = "ID2",
            artist = "Nekrogoblikon",
            city = "Silver Spring",
            state = "MD",
            venueName = "The Fillmore Silver Spring",
            date = Date()
        ),
        Show(
            id = "ID3",
            artist = "Dethklok",
            city = "Silver Spring",
            state = "MD",
            venueName = "The Fillmore Silver Spring",
            date = Date()
        )
    )

    @AssistedFactory
    interface Factory {
        fun create(
            showId: String,
        ): ShowViewModel
    }
}

sealed interface ShowUiState {
    data object Loading : ShowUiState
    data class Loaded(
        val show: Show,
        val tracks: Array<Track>,
        val linkedShows: Array<Show>
    ) : ShowUiState
}
