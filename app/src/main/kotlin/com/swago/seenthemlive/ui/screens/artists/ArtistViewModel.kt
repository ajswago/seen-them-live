package com.swago.seenthemlive.ui.screens.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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

@HiltViewModel(assistedFactory = ArtistViewModel.Factory::class)
class ArtistViewModel @AssistedInject constructor(
    @Assisted val artistId: String,
    firebaseRepository: FirebaseRepository
) : ViewModel() {

    val artistFlow: Flow<Artist> = flow {
        val user = Firebase.auth.currentUser
        emit(firebaseRepository.getArtist(user?.uid ?: "", artistId = artistId))
    }

    val showsFlow: Flow<List<GroupedShow>> = flow {
        val user = Firebase.auth.currentUser
        emit(firebaseRepository.getShowsForArtist(user?.uid ?: "", artistId = artistId))
    }

    val tracksFlow: Flow<List<Track>> = flow {
        val user = Firebase.auth.currentUser
        emit(firebaseRepository.getTracksForArtist(user?.uid ?: "", artistId = artistId))
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
