package com.swago.seenthemlive.ui.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.firebase.firestore.UserRepository
import com.swago.seenthemlive.ui.common.ArtistItem
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ArtistViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _artists = MutableLiveData<List<ArtistItem>>().apply {
        value = ArrayList()
    }
    val artists: MutableLiveData<List<ArtistItem>> = _artists

    private fun setArtists(artistsByCount: List<ArtistItem>) {
        artists.postValue(artistsByCount.sortedBy { it.artistName })
    }

    fun fetchUser(userId: String, completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val artistsByCount = user?.setlists
                ?.groupBy { it.artist }
                ?.map {
                    val sortedSetlists = it.value.sortedBy { setlist -> setlist.eventDate }
                    val firstSetlist = sortedSetlists.first()
                    val lastSetlist = sortedSetlists.last()
                    ArtistItem( it.key?.mbid, it.key?.name, firstSetlist.eventDate, lastSetlist.eventDate, firstSetlist.venue?.name, lastSetlist.venue?.name, it.value.size)
                }

            setArtists(artistsByCount ?: ArrayList())

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }
}