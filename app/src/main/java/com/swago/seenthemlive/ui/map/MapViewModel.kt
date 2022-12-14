package com.swago.seenthemlive.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.firebase.firestore.UserRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MapViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _userMapItems = MutableLiveData<List<MapItem>>().apply {
        value = ArrayList()
    }
    val userMapItems: MutableLiveData<List<MapItem>> = _userMapItems

    private fun setMapItems(mapItems: List<MapItem>) {
        userMapItems.postValue(mapItems)
    }

    fun fetchUser(userId: String, completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val mapItems = user?.setlists?.groupBy {
                it.venue
            }?.map { venueGroup ->
                MapItem(venueGroup.key?.name, venueGroup.value.groupBy { it.eventDate }.values.count(),
                venueGroup.key?.city?.coords?.lat, venueGroup.key?.city?.coords?.long) }
            setMapItems(mapItems ?: ArrayList())

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }
}

data class MapItem(
    val name: String? = null,
    val count: Int? = null,
    val lat: Double? = null,
    val long: Double? = null
)