package com.swago.seenthemlive.ui.screens.search

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
import com.swago.seenthemlive.ui.screens.shows.ConfirmationUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SearchResultViewModel.Factory::class)
class SearchResultViewModel @AssistedInject constructor(
    @Assisted val showId: String,
    setlistFmRepository: SetlistFmRepository,
    val firebaseRepository: FirebaseRepository,
) : ViewModel() {

    val showFlow: Flow<ShowData> = flow {
        emit(setlistFmRepository.getShow(showId = showId))
    }

    val savedFlow: Flow<Boolean> = flow {
        emit(firebaseRepository.showSaved(showId = showId))
    }

    var uiState: SearchResultUiState by mutableStateOf(SearchResultUiState.Loading)
    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            combine(
                showFlow,
                savedFlow
            ) { showData, saved ->
                SearchResultUiState.Loaded(
                    showData.show,
                    saved,
                    showData.tracks,
                    showData.encore
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchResultUiState.Loading
            ).collect { uiState = it }
        }
    }

    var confirmationUiState: ConfirmationUiState by mutableStateOf(ConfirmationUiState.NotLoading)

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
