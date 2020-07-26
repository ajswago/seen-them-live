package com.swago.seenthemlive.ui.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.firebase.firestore.UserRepository
import com.swago.seenthemlive.ui.common.UserItem
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FriendsViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _friends = MutableLiveData<List<UserItem>>().apply {
        value = ArrayList()
    }
    val friends: MutableLiveData<List<UserItem>> = _friends

    fun fetchFriends(completion: () -> Unit) {
        scope.launch {
            val users = UserRepository.getAllUsers()

            friends.postValue(users.map { UserItem(it.id, it.email, it.displayName) })

            GlobalScope.launch(Dispatchers.Main) {
                completion()
            }
        }
    }
}