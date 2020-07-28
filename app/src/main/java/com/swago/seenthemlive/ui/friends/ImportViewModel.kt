package com.swago.seenthemlive.ui.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.firebase.firestore.UserRepository
import com.swago.seenthemlive.ui.search.SetlistItem
import com.swago.seenthemlive.util.Utils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ImportViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _importSetlists = MutableLiveData<List<SetlistItem>>().apply {
        value = ArrayList()
    }
    val importSetlists: MutableLiveData<List<SetlistItem>> = _importSetlists

    fun fetchSetlists(userId: String, importProfileId: String, completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val importProfile = UserRepository.getUser(importProfileId)

            val userSetlistIds = user?.setlists?.map { it.id } ?: ArrayList()
            val filteredSetlists = importProfile?.setlists
                ?.filterNot { importSetlist -> userSetlistIds.contains(importSetlist.id) }

            importSetlists.postValue(filteredSetlists?.sortedByDescending { it.eventDate }?.map {
                var location: String
                it.venue.let {venue -> location = Utils.formatVenueString(venue!!) }
                SetlistItem(it.id, it.artist?.name, location, it.tour?.name, it.eventDate) } ?: ArrayList())

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }
}