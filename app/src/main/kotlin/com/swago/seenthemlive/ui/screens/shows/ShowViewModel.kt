package com.swago.seenthemlive.ui.screens.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
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

@HiltViewModel(assistedFactory = ShowViewModel.Factory::class)
class ShowViewModel @AssistedInject constructor(
    @Assisted val showId: String,
    firebaseRepository: FirebaseRepository
) : ViewModel() {

    val showFlow: Flow<Show> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getShow(showId = showId))
    }

    val savedFlow: Flow<Boolean> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.showSaved(showId = showId))
    }

    val tracksFlow: Flow<List<Track>> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getTracksForShow(showId = showId))
    }

    val encoreTracksFlow: Flow<List<Track>> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getEncoreTracksForShow(showId = showId))
    }

    val linkedShowsFlow: Flow<List<Show>> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getLinkedShows(showId = showId))
    }

    val uiState: StateFlow<ShowUiState> = combine(
        showFlow,
        savedFlow,
        tracksFlow,
        encoreTracksFlow,
        linkedShowsFlow,
        ShowUiState::Loaded
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShowUiState.Loading
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
        val saved: Boolean,
        val tracks: List<Track>,
        val encoreTracks: List<Track>,
        val linkedShows: List<Show>
    ) : ShowUiState
}
