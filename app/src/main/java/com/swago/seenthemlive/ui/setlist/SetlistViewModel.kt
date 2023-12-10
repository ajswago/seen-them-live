package com.swago.seenthemlive.ui.setlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.firebase.firestore.UserRepository
import com.swago.seenthemlive.ui.common.SongItem
import com.swago.seenthemlive.util.Utils
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class SetlistViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _isSaved = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val _artist = MutableLiveData<String>().apply {
        value = ""
    }
    private val _date = MutableLiveData<String>().apply {
        value = ""
    }
    private val _venue = MutableLiveData<String>().apply {
        value = ""
    }
    private val _tour = MutableLiveData<String>().apply {
        value = ""
    }
    private val _songList = MutableLiveData<List<SongItem>>().apply {
        value = ArrayList()
    }
    private val _encoreList = MutableLiveData<List<SongItem>>().apply {
        value = ArrayList()
    }
    private val _encore2List = MutableLiveData<List<SongItem>>().apply {
        value = ArrayList()
    }
    private val _encore3List = MutableLiveData<List<SongItem>>().apply {
        value = ArrayList()
    }

    val isSaved: MutableLiveData<Boolean> = _isSaved
    val artist: MutableLiveData<String> = _artist
    val date: MutableLiveData<String> = _date
    val venue: MutableLiveData<String> = _venue
    val tour: MutableLiveData<String> = _tour
    val songList: MutableLiveData<List<SongItem>> = _songList
    val encoreList: MutableLiveData<List<SongItem>> = _encoreList
    val encore2List: MutableLiveData<List<SongItem>> = _encore2List
    val encore3List: MutableLiveData<List<SongItem>> = _encore3List

    fun fetchUser(userId: String, setlist: Setlist, completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val savedToUser = user?.setlists?.any { setlistItem -> setlistItem.id == setlist.id }
            val eventDateString = setlist
                .eventDate.let{ Utils.formatDateString(setlist.eventDate!!) }
            val venueString = setlist
                .venue.let{ Utils.formatVenueString(setlist.venue!!) }
            val songItems = ArrayList<SongItem>()
            val encoreItems = ArrayList<SongItem>()
            val encore2Items = ArrayList<SongItem>()
            val encore3Items = ArrayList<SongItem>()
            setlist.sets?.set?.forEach { set ->
                val songsWithoutTapes = set.song?.filter { it.tape?.not() ?: true }
                set.song?.map { song ->
                    var index: Int? = null
                    val indexInList = songsWithoutTapes?.indexOf(song) ?: -1
                    if (indexInList >= 0) {
                        index = indexInList + 1
                    }
                    val songItem = SongItem(
                        index,
                        song.name,
                        song.cover?.name
                    )
                    if (set.encore == 1)
                        encoreItems.add(songItem)
                    else if (set.encore == 2)
                        encore2Items.add(songItem)
                    else if (set.encore == 3)
                        encore3Items.add(songItem)
                    else
                        songItems.add(songItem)
                }
            }

            isSaved.postValue(savedToUser ?: false)
            artist.postValue(setlist.artist?.name)
            date.postValue(eventDateString)
            venue.postValue(venueString)
            tour.postValue(setlist.tour?.name)
            songList.postValue(songItems)
            encoreList.postValue(encoreItems)
            encore2List.postValue(encore2Items)
            encore3List.postValue(encore3Items)

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }

    fun saveSetlistToUser(userId: String, setlist: Setlist,
                          setlistSaved: Boolean,
                          completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val savedSetlists = ArrayList<Setlist>()
            savedSetlists.addAll(user?.setlists ?: ArrayList())
            if (setlistSaved) {
                savedSetlists.add(setlist)
                user?.setlists = savedSetlists
            } else {
                savedSetlists.removeAll { setlistItem -> setlist.id == setlistItem.id }
                user?.setlists = savedSetlists
            }
            user.let{ UserRepository.setUser(it!!) }

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }

    fun overwriteSetlistToUser(userId: String, setlist: Setlist,
                          completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val savedSetlists = ArrayList<Setlist>()
            savedSetlists.addAll(user?.setlists ?: ArrayList())
            savedSetlists.removeAll { setlistItem -> setlist.id == setlistItem.id }
            savedSetlists.add(setlist)
            user?.setlists = savedSetlists
            user.let{ UserRepository.setUser(it!!) }

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }
}