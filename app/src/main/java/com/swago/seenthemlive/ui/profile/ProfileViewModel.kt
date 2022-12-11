package com.swago.seenthemlive.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.firebase.firestore.UserRepository
import com.swago.seenthemlive.ui.common.CountedItem
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _userDisplayName = MutableLiveData<String>().apply {
        value = "First Last"
    }
    val userDisplayName: MutableLiveData<String> = _userDisplayName

    private val _userUsername = MutableLiveData<String>().apply {
        value = "usr"
    }
    val userUsername: MutableLiveData<String> = _userUsername

    private val _userEmail = MutableLiveData<String>().apply {
        value = "first.last@email.com"
    }
    val userEmail: MutableLiveData<String> = _userEmail

    private val _userConcertCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val userConcertCount: MutableLiveData<Int> = _userConcertCount

    private val _userBandCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val userBandCount: MutableLiveData<Int> = _userBandCount

    private val _userVenueCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val userVenueCount: MutableLiveData<Int> = _userVenueCount

    private val _userTopArtists = MutableLiveData<List<CountedItem>>().apply {
        value = ArrayList()
    }
    val userTopArtists: MutableLiveData<List<CountedItem>> = _userTopArtists

    private fun setTopArtists(artistsByCount: List<CountedItem>) {
        userTopArtists.postValue(artistsByCount.sortedWith(compareByDescending<CountedItem> { it.count }.thenBy { it.name }).take(20))
    }

    fun fetchUser(userId: String, completion: () -> Unit) {
        scope.launch {
            val user = UserRepository.getUser(userId)
            val concertCount = user?.setlists?.groupBy {
                it.eventDate
            }?.size
            val bandCount = user?.setlists?.groupBy {
                it.artist?.mbid
            }?.size
            val venueCount = user?.setlists?.groupBy {
                it.venue?.id
            }?.size
            val artistsByCount = user?.setlists
                ?.groupBy { it.artist?.name }
                ?.map { CountedItem(it.key, it.value.size) }

            userDisplayName.postValue(user?.displayName)
            userUsername.postValue(user?.username)
            userEmail.postValue(user?.email)
            userConcertCount.postValue(concertCount)
            userBandCount.postValue(bandCount)
            userVenueCount.postValue(venueCount)
            setTopArtists(artistsByCount ?: ArrayList())

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }

}
