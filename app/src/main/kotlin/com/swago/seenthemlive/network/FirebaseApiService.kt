package com.swago.seenthemlive.network

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import javax.inject.Inject

interface FirebaseApiService {
    suspend fun getUser(uid: String): UserData
    suspend fun toggleSaved(showId: String): UserData
}

class NetworkFirebaseApiService @Inject constructor() : FirebaseApiService {
    override suspend fun getUser(uid: String): UserData {
        val firestore = FirebaseFirestore.getInstance()
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            snapshot.toObject(UserData::class.java) ?: UserData()
        } catch (e: Exception) {
            UserData()
        }
    }
    override suspend fun toggleSaved(showId: String): UserData {
        return UserData()
    }
}

class FakeFirebaseApiService @Inject constructor() : FirebaseApiService {
    override suspend fun getUser(uid: String): UserData {
        return FakeFirebaseDataSource.user
    }
    override suspend fun toggleSaved(showId: String): UserData {
        return FakeFirebaseDataSource.user
    }
}

data class UserData(
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var displayName: String? = null,
    var setlists: List<Setlist>? = null
) : Serializable
