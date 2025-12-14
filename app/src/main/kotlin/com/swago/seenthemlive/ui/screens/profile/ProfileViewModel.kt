package com.swago.seenthemlive.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    val profileFlow: Flow<Profile> = flow {
        delay(Duration.ofMillis(2000))
        emit(profile)
    }

    val artistsFlow: Flow<Array<Artist>> = flow {
        delay(Duration.ofMillis(2000))
        emit(artists)
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

    val profile = Profile(
        name = "Anthony Swago",
        email = "ajswago@gmail.com",
        showCount = 72,
        venueCount = 34,
        artistCount = 78
    )

    val artists = arrayOf(
        Artist(
            "ID1",
            name = "Lamb of God",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID2",
            name = "Megadeth",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID3",
            name = "Opeth",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID4",
            name = "Iron Maiden",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 4
        )
    )
}

sealed interface ProfileUiState {
    data object Loading: ProfileUiState
    data class Loaded(
        val profile: Profile,
        val artists: Array<Artist>
    ) : ProfileUiState
}
