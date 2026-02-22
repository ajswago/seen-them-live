package com.swago.seenthemlive.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.data.repository.SetlistFmRepository
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
import java.time.Duration

@HiltViewModel(assistedFactory = SearchResultViewModel.Factory::class)
class SearchResultViewModel @AssistedInject constructor(
    @Assisted val showId: String,
    setlistFmRepository: SetlistFmRepository,
    firebaseRepository: FirebaseRepository,
) : ViewModel() {

    val showFlow: Flow<Show> = flow {
        delay(Duration.ofMillis(2000))
        emit(setlistFmRepository.getShow(showId = showId))
    }

    val savedFlow: Flow<Boolean> = flow {
        val user = Firebase.auth.currentUser
        emit(firebaseRepository.showSaved(user?.uid ?: "", showId = showId))
    }

    val tracksFlow: Flow<List<Track>> = flow {
        delay(Duration.ofMillis(2000))
        emit(setlistFmRepository.getTracksForShow(showId = showId))
    }

    val encoreTracksFlow: Flow<List<Track>> = flow {
        delay(Duration.ofMillis(2000))
        emit(setlistFmRepository.getEncoreTracksForShow(showId = showId))
    }

    val uiState: StateFlow<SearchResultUiState> = combine(
        showFlow,
        savedFlow,
        tracksFlow,
        encoreTracksFlow,
        SearchResultUiState::Loaded
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchResultUiState.Loading
    )

    @AssistedFactory
    interface Factory {
        fun create(
            showId: String,
        ): SearchResultViewModel
    }
}

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    data class Loaded(
        val show: Show,
        val saved: Boolean,
        val tracks: List<Track>,
        val encoreTracks: List<Track>,
    ) : SearchResultUiState
}
