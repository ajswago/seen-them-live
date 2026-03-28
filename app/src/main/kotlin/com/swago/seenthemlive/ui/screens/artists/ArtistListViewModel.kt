package com.swago.seenthemlive.ui.screens.artists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.models.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {

    val artistsFlow: Flow<List<Artist>> = flow {
        val artists = firebaseRepository.getArtists()
        emit(artists)
    }

    var uiState: ArtistListUiState by mutableStateOf(ArtistListUiState.Loading)

    fun load() {
        viewModelScope.launch {
            artistsFlow.map { artists ->
                ArtistListUiState.Loaded(artists)
            }.stateIn(
                scope = viewModelScope,
                initialValue = ArtistListUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            ).collect { uiState = it }
        }
    }
}

sealed interface ArtistListUiState {
    data object Loading : ArtistListUiState
    data class Loaded(
        val artists: List<Artist>
    ) : ArtistListUiState
}
