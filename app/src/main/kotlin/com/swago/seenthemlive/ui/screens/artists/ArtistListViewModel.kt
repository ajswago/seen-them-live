package com.swago.seenthemlive.ui.screens.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.models.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {

    val artistsFlow: Flow<List<Artist>> = flow {
        val user = Firebase.auth.currentUser
        emit(firebaseRepository.getArtists(user?.uid ?: ""))
    }

    val uiState: StateFlow<ArtistListUiState> =
        artistsFlow.map {
            ArtistListUiState.Loaded(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = ArtistListUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface ArtistListUiState {
    data object Loading : ArtistListUiState
    data class Loaded(
        val artists: List<Artist>
    ) : ArtistListUiState
}
