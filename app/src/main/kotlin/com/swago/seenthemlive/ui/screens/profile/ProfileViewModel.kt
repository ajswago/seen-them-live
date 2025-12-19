package com.swago.seenthemlive.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {

    val profileFlow: Flow<Profile> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getProfile())
    }

    val artistsFlow: Flow<List<Artist>> = flow {
        delay(Duration.ofMillis(2000))
        emit(firebaseRepository.getTopArtistsForProfile())
    }

    val uiState: StateFlow<ProfileUiState> = combine(
        profileFlow,
        artistsFlow,
        ProfileUiState::Loaded
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState.Loading
    )
}

sealed interface ProfileUiState {
    data object Loading: ProfileUiState
    data class Loaded(
        val profile: Profile,
        val artists: List<Artist>
    ) : ProfileUiState
}
