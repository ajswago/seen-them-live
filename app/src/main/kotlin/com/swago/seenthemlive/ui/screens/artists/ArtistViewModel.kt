package com.swago.seenthemlive.ui.screens.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
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
import java.time.Duration

@HiltViewModel(assistedFactory = ArtistViewModel.Factory::class)
class ArtistViewModel @AssistedInject constructor(
    @Assisted val artistId: String,
    firebaseRepository: FirebaseRepository
) : ViewModel() {

    val artistFlow: Flow<Artist> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getArtist(artistId = artistId))
    }

    val showsFlow: Flow<List<GroupedShow>> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getShowsForArtist(artistId = artistId))
    }

    val tracksFlow: Flow<List<Track>> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getTracksForArtist(artistId = artistId))
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
        val shows: List<GroupedShow>,
        val tracks: List<Track>
    ) : ArtistUiState
}
