package com.swago.seenthemlive.ui.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.api.setlistfm.Song
import com.swago.seenthemlive.firebase.firestore.UserRepository
import com.swago.seenthemlive.ui.common.ConcertItem
import com.swago.seenthemlive.ui.common.CountedItem
import com.swago.seenthemlive.util.Utils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ArtistDetailViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _artistName = MutableLiveData<String>().apply {
        value = "First Last"
    }
    val artistName: MutableLiveData<String> = _artistName

    private val _artistSetlistCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val artistSetlistCount: MutableLiveData<Int> = _artistSetlistCount

    private val _artistConcerts = MutableLiveData<List<ConcertItem>>().apply {
        value = ArrayList()
    }
    val artistConcerts: MutableLiveData<List<ConcertItem>> = _artistConcerts

    private val _artistTopSongs = MutableLiveData<List<CountedItem>>().apply {
        value = ArrayList()
    }
    val artistTopSongs: MutableLiveData<List<CountedItem>> = _artistTopSongs

    private fun setArtistSetlists(setlists: List<ConcertItem>) {
        artistConcerts.postValue(setlists.sortedBy { it.date })
    }

    private fun setArtistTopSongs(songsByCount: List<CountedItem>) {
        artistTopSongs.postValue(songsByCount.sortedByDescending { it.count })
    }

    fun fetchUserArtist(userId: String, artistId: String, completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val setlists = user?.setlists?.groupBy {
                it.artist?.mbid
            }?.get(artistId)
            val setlistCount = setlists?.size
            val concertItems = user?.setlists?.groupBy { it.eventDate }
                ?.map { group ->
                    var location: String
                    val venue = group.value.first().venue
                    venue.let { location = Utils.formatLocationString(venue!!) }
                    ConcertItem(
                        group.value.first().venue?.name,
                        location,
                        group.key,
                        group.value.map { it.artist?.name ?: "" })
                }?.filter { concertItem -> concertItem.artists?.contains(setlists?.first()?.artist?.name) ?: false }
            val allSongs = setlists?.flatMap { setlist -> setlist.sets?.set?.flatMap { set -> set.song?.map { it } ?: ArrayList<Song>() } ?: ArrayList<Song>() }
            val songsWithoutTapes = allSongs?.filter { it.tape?.not() ?: true }
            val songsByCount = songsWithoutTapes?.groupBy { it.name }?.map { CountedItem(it.key, it.value.size) }

            artistName.postValue(setlists?.first()?.artist?.name)
            artistSetlistCount.postValue(setlistCount)
            setArtistSetlists(concertItems ?: ArrayList())
            setArtistTopSongs(songsByCount ?: ArrayList())

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }

}
