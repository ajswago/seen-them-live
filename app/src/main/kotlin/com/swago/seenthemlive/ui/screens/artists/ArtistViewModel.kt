package com.swago.seenthemlive.ui.screens.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
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

@HiltViewModel(assistedFactory = ArtistViewModel.Factory::class)
class ArtistViewModel @AssistedInject constructor(
    @Assisted val artistId: String,
) : ViewModel() {

    val artistFlow: Flow<Artist> = flow {
        delay(Duration.ofMillis(2000))
        emit(artist)
    }

    val showsFlow: Flow<Array<GroupedShow>> = flow {
        delay(Duration.ofMillis(2000))
        emit(shows)
    }

    val tracksFlow: Flow<Array<Track>> = flow {
        delay(Duration.ofMillis(2000))
        emit(tracks)
    }

    val uiState: StateFlow<ArtistUiState> = combine(
        artistFlow,
        showsFlow,
        tracksFlow,
        ArtistUiState::Loaded
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArtistUiState.Loading
    )

    val shows = arrayOf(
        GroupedShow(
            id = "ID1",
            venueName = "Jiffy Lube Live",
            city = "Bristow",
            state = "VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date(),
            artists = arrayOf("Anthrax", "Behemoth", "Slayer", "Testament")
        ),
        GroupedShow(
            id = "ID2",
            venueName = "PPL Center",
            city = "Allentown",
            state = "PA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            arrayOf("Megadeth", "Trivium")
        )
    )
    val tracks = arrayOf(
        Track("Ruin", trackCount = 5),
        Track("Walk With Me in Hell", trackCount = 5),
        Track("Now You've Got Something to Die For", trackCount = 5),
        Track("Laid to Rest", trackCount = 5),
        Track("Redneck", trackCount = 3),
        Track("512", trackCount = 3)
    )
    val artist = Artist(
        id = "ID1",
        name = "Lamb of God",
        lastShow = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2022-06-10") ?: Date(),
    )

    @AssistedFactory
    interface Factory {
        fun create(
            artistId: String,
        ): ArtistViewModel
    }
}

sealed interface ArtistUiState {
    data object Loading : ArtistUiState
    data class Loaded(
        val artist: Artist,
        val shows: Array<GroupedShow>,
        val tracks: Array<Track>
    ) : ArtistUiState
}
