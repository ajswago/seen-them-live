package com.swago.seenthemlive.ui.screens.shows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.data.repository.SetlistFmRepository
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.util.Date

@HiltViewModel(assistedFactory = ShowViewModel.Factory::class)
class ShowViewModel @AssistedInject constructor(
    @Assisted val showId: String,
    val firebaseRepository: FirebaseRepository,
    val setlistFmRepository: SetlistFmRepository
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

    fun toggleSavedFlow(id: String): Flow<Boolean> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.toggleSaved(showId = showId))
    }

    var uiState: ShowUiState by mutableStateOf(ShowUiState.Loading)
    init {
        viewModelScope.launch {
            combine(
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
            ).collect { uiState = it }
        }
    }

    var relatedShowsResultsUiState: RelatedShowsResultsUiState by mutableStateOf(
        RelatedShowsResultsUiState.Hidden)

    var searchJob: Job? = null

    fun performSearch(date: Date, venue: String) {
        relatedShowsResultsUiState = RelatedShowsResultsUiState.Loading
        searchJob = viewModelScope.launch {
            delay(Duration.ofMillis(2000))
            relatedShowsResultsUiState = RelatedShowsResultsUiState.Results(
                shows = setlistFmRepository.getSearchResults(date, venue)
            )
        }
    }

    fun dismissBottomSheet() {
        searchJob?.cancel()
        relatedShowsResultsUiState = RelatedShowsResultsUiState.Hidden
    }

    fun toggleSaved(id: String) {
        uiState = ShowUiState.Loading
        viewModelScope.launch {
            combine(
                showFlow,
                toggleSavedFlow(id),
                tracksFlow,
                encoreTracksFlow,
                linkedShowsFlow,
                ShowUiState::Loaded
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ShowUiState.Loading
            ).collect { uiState = it }
        }
    }

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

sealed interface RelatedShowsResultsUiState {
    data object Hidden : RelatedShowsResultsUiState
    data object Loading : RelatedShowsResultsUiState
    data class Results(
        val shows: List<Show>,
    ) : RelatedShowsResultsUiState
}
