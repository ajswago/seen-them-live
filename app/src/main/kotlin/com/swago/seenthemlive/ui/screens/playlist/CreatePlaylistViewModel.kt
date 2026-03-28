package com.swago.seenthemlive.ui.screens.playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.swago.seenthemlive.BuildConfig
import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.data.repository.NetworkSpotifyRepository
import com.swago.seenthemlive.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository,
    val spotifyRepository: NetworkSpotifyRepository
) : ViewModel() {

    private val scopes =
        "user-read-email,user-read-private,playlist-modify-private,playlist-modify-public"

    var code: String? = null
    var userId: String? = null

    var createPlaylistStep: CreatePlaylistStep by mutableStateOf(CreatePlaylistStep.NOT_STARTED)

    val showsFlow: Flow<List<Show>> = flow {
        emit(firebaseRepository.getShows())
    }

    var uiState: CreatePlaylistUiState by mutableStateOf(CreatePlaylistUiState.Loading)

    fun load() {
        viewModelScope.launch {
            showsFlow.map {
                CreatePlaylistUiState.Loaded(it)
            }.stateIn(
                scope = viewModelScope,
                initialValue = CreatePlaylistUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            ).collect { uiState = it }
        }
    }

    fun checkSpotifyAuth(
        getSavedCode: () -> String?,
        updateCode: (String) -> Unit,
        authenticateWithSpotify: (AuthorizationRequest, (String) -> Unit) -> Unit
    ) {
        code = getSavedCode()
        code?.let { code ->
            viewModelScope.launch {
                val profile = spotifyRepository.getProfile(code)
                if (profile == null) {
                    viewModelScope.launch(Dispatchers.Main) {
                        authenticateWithSpotify(spotifyRequest()) { code ->
                            this@CreatePlaylistViewModel.code = code
                            viewModelScope.launch {
                                val profile = spotifyRepository.getProfile(code)
                                userId = profile?.id
                                updateCode(code)
                                load()
                            }
                        }
                    }
                } else {
                    userId = profile.id
                    load()
                }
            }
        } ?: authenticateWithSpotify(spotifyRequest()) { code ->
            this.code = code
            viewModelScope.launch {
                val profile = spotifyRepository.getProfile(code)
                userId = profile?.id
                updateCode(code)
                load()
            }
        }

    }

    fun spotifyRequest(): AuthorizationRequest {
        val builder = AuthorizationRequest.Builder(
            BuildConfig.SPOTIFY_CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            "com.swago.seenthemlive://callback/redirect")

        builder.setScopes(arrayOf(scopes))
        return builder.build()
    }

    fun createPlaylist(name: String, selections: List<Show>) {
        createPlaylistStep = CreatePlaylistStep.CREATE_PLAYLIST
        try {
            viewModelScope.launch {
                code?.let { code ->
                    val playlistId = spotifyRepository.createPlaylist(code, userId ?: "", name)
                    if (playlistId == null) {
                        createPlaylistStep = CreatePlaylistStep.FAILED
                        return@launch
                    }
                    createPlaylistStep = CreatePlaylistStep.FIND_SONGS
                    val songIds = selections.flatMap { show ->
                        val showData = firebaseRepository.getShow(show.id)
                        showData.tracks.plus(showData.encore)
                            .mapNotNull { track ->
                                spotifyRepository.searchSong(
                                    code,
                                    show.artist,
                                    track.trackName
                                )
                            }
                    }
                    createPlaylistStep = CreatePlaylistStep.ADD_SONGS
                    val result = spotifyRepository.addSongsToPlaylist(code, playlistId, songIds)
                    createPlaylistStep = if (!result.isNullOrEmpty())
                        CreatePlaylistStep.COMPLETE
                    else
                        CreatePlaylistStep.FAILED
                } ?: run {
                    createPlaylistStep = CreatePlaylistStep.FAILED
                }
            }
        } catch (_ : HttpException) {
            createPlaylistStep = CreatePlaylistStep.FAILED
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
