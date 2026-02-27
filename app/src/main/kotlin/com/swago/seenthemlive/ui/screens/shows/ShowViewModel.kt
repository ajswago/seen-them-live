package com.swago.seenthemlive.ui.screens.shows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.data.repository.SetlistFmRepository
import com.swago.seenthemlive.data.repository.ShowData
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel(assistedFactory = ShowViewModel.Factory::class)
class ShowViewModel @AssistedInject constructor(
    @Assisted val showId: String,
    val firebaseRepository: FirebaseRepository,
    val setlistFmRepository: SetlistFmRepository
) : ViewModel() {

    val showFlow: Flow<ShowData> = flow {
        emit(firebaseRepository.getShow(showId = showId))
    }

    val savedFlow: Flow<Boolean> = flow {
        emit(firebaseRepository.showSaved(showId = showId))
    }

    val linkedShowsFlow: Flow<List<Show>> = flow {
        emit(firebaseRepository.getLinkedShows(showId = showId))
    }

    var uiState: ShowUiState by mutableStateOf(ShowUiState.Loading)
    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            combine(
                showFlow,
                savedFlow,
                linkedShowsFlow,
            ) { showData, saved, linkedShows ->
                ShowUiState.Loaded(
                    showData.show,
                    saved,
                    showData.tracks,
                    showData.encore,
                    linkedShows
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ShowUiState.Loading
            ).collect { uiState = it }
        }
    }

    var relatedShowsResultsUiState: RelatedShowsResultsUiState by mutableStateOf(
        RelatedShowsResultsUiState.Hidden)

    var confirmationUiState: ConfirmationUiState by mutableStateOf(ConfirmationUiState.NotLoading)

    var searchJob: Job? = null

    fun performSearch(date: Date, venue: String) {
        relatedShowsResultsUiState = RelatedShowsResultsUiState.Loading
        searchJob = viewModelScope.launch {
            relatedShowsResultsUiState = RelatedShowsResultsUiState.Results(
                shows = setlistFmRepository.getSearchResults(date, venue)
            )
        }
    }

    fun dismissBottomSheet() {
        searchJob?.cancel()
        relatedShowsResultsUiState = RelatedShowsResultsUiState.Hidden
    }

    fun toggleSaved(completion: () -> Unit) {
        confirmationUiState = ConfirmationUiState.Loading
        viewModelScope.launch {
            val saved = firebaseRepository.showSaved(showId = showId)
            if (saved) {
                firebaseRepository.removeShow(showId = showId)
            } else {
                firebaseRepository.saveShow(showId = showId)
            }
        }.invokeOnCompletion {
            viewModelScope.launch(Dispatchers.Main) {
                load()
                confirmationUiState = ConfirmationUiState.NotLoading
                completion()
            }
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

sealed interface ConfirmationUiState {
    data object NotLoading : ConfirmationUiState
    data object Loading : ConfirmationUiState
}
