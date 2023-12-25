package com.swago.seenthemlive.ui.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.api.spotify.PlaylistRepository
import com.swago.seenthemlive.api.spotify.ProfileRepository
import com.swago.seenthemlive.api.spotify.SpotifyApiBuilder
import com.swago.seenthemlive.firebase.firestore.UserRepository
import com.swago.seenthemlive.ui.search.SetlistItem
import com.swago.seenthemlive.util.Utils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CreatePlaylistViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _createPlaylistSetlists = MutableLiveData<List<SetlistItem>>().apply {
        value = ArrayList()
    }
    val createPlaylistSetlists: MutableLiveData<List<SetlistItem>> = _createPlaylistSetlists

    fun fetchSetlists(userId: String, completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)

            createPlaylistSetlists.postValue(user?.setlists?.sortedByDescending { it.eventDate }?.map {
                var location: String
                it.venue.let {venue -> location = Utils.formatVenueString(venue!!) }
                SetlistItem(it.id, it.artist?.name, location, it.tour?.name, it.eventDate) } ?: ArrayList())

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }

    fun createPlaylistFromSelections(token: String, userId: String, setlistIds: List<String>, playlistName: String, completion: () -> Unit) {
        val profileRepository = ProfileRepository(SpotifyApiBuilder.profile(token))
        val repository = PlaylistRepository(SpotifyApiBuilder.playlist(token))
        scope.launch {
            val user = UserRepository.getUser(userId)

            val selectedSetlists = user?.setlists
                ?.filter { setlist -> setlistIds.contains(setlist.id) } ?: ArrayList()

            val profile = profileRepository.getProfile()
            profile?.id?.let {profileId ->

                val songUris = arrayListOf<String>()
                selectedSetlists.forEach { setlist ->
                    setlist.sets?.set?.forEach { set ->
                        set.song?.forEach { song ->
                            if (song.tape != true) {
                                val songName = song.name
                                val artistName = setlist.artist?.name
                                if (songName != null && artistName != null) {
                                    val uri = repository.searchSong(
                                        songName,
                                        artistName
                                    )?.tracks?.items?.firstOrNull()?.uri
                                    uri?.let {
                                        songUris.add(uri)
                                    }
                                }
                            }
                        }
                    }
                }
                val playlist = repository.createPlaylist(profileId, playlistName)
                playlist?.id?.let {playlistId ->
                    songUris.chunked(100).forEach { uriGroup ->
                        repository.addSongsToPlaylist(playlistId, uriGroup)
                    }
                }
            }
            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }
}