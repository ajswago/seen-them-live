package com.swago.seenthemlive.ui.screens.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShowsListViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {

    val showsFlow: Flow<List<Show>> = flow {
        val user = Firebase.auth.currentUser
        emit(firebaseRepository.getShows(user?.uid ?: ""))
    }

    val uiState: StateFlow<ShowsListUiState> =
        showsFlow.map {
            ShowsListUiState.Loaded(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = ShowsListUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface ShowsListUiState {
    data object Loading : ShowsListUiState
    data class Loaded(
        val shows: List<Show>
    ) : ShowsListUiState
}
