package com.swago.seenthemlive.ui.screens.playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.data.repository.NetworkSpotifyRepository
import com.swago.seenthemlive.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository,
    val spotifyRepository: NetworkSpotifyRepository
) : ViewModel() {

    var createPlaylistStep: CreatePlaylistStep by mutableStateOf(CreatePlaylistStep.NOT_STARTED)

    val showsFlow: Flow<List<Show>> = flow {
        emit(firebaseRepository.getShows())
    }

    var uiState: StateFlow<CreatePlaylistUiState> =
        showsFlow.map {
            CreatePlaylistUiState.Loaded(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = CreatePlaylistUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    fun createPlaylist(name: String, selections: List<Show>) {
        createPlaylistStep = CreatePlaylistStep.CREATE_PLAYLIST
        viewModelScope.launch {
            delay(Duration.ofMillis(2000))
            val playlistId = spotifyRepository.createPlaylist(name)
            if (playlistId == null) {
                createPlaylistStep = CreatePlaylistStep.FAILED
                return@launch
            }
            createPlaylistStep = CreatePlaylistStep.FIND_SONGS
            delay(Duration.ofMillis(2000))
            val songIds = selections.flatMap { show ->
                val showData = firebaseRepository.getShow(show.id)
                showData.tracks.plus(showData.encore)
                    .mapNotNull { track -> spotifyRepository.searchSong(show.artist, track.trackName) }
            }
            createPlaylistStep = CreatePlaylistStep.ADD_SONGS
            delay(Duration.ofMillis(2000))
            val result = spotifyRepository.addSongsToPlaylist(playlistId, songIds)
            createPlaylistStep = CreatePlaylistStep.COMPLETE
        }
    }
}

sealed interface CreatePlaylistUiState {
    data object Loading : CreatePlaylistUiState
    data class Loaded(
        val shows: List<Show>
    ) : CreatePlaylistUiState
}

enum class CreatePlaylistStep(var progress: Float, var description: String) {
    NOT_STARTED(0.0f, ""),
    CREATE_PLAYLIST(0.25f, "Creating new playlist in your Spotify account."),
    FIND_SONGS(0.5f, "Looking up selected songs."),
    ADD_SONGS(0.75f, "Adding matched songs to newly created playlist."),
    COMPLETE(1.0f, "Playlist created successfully. Enjoy listening on Spotify!"),
    FAILED(progress = 1.0f, "Failed to create playlist.")
}
