package com.swago.seenthemlive.network

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

interface FirebaseApiService {
    suspend fun getUser(): UserData
    suspend fun saveShow(setlist: Setlist): UserData
    suspend fun removeShow(showId: String): UserData
}

class NetworkFirebaseApiService @Inject constructor() : FirebaseApiService {
    override suspend fun getUser(): UserData {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
        val userId = Firebase.auth.currentUser?.uid ?: ""
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            snapshot.toObject(UserData::class.java) ?: UserData()
        } catch (ce: CancellationException) {
            throw ce // Important: rethrow the cancellation exception
        } catch (_: Exception) {
            UserData()
        }
    }
    override suspend fun saveShow(setlist: Setlist): UserData {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
        val userId = Firebase.auth.currentUser?.uid ?: ""
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            val userData = snapshot.toObject(UserData::class.java) ?: UserData()
            val updatedSetlists = userData.setlists?.toMutableList()
            updatedSetlists?.add(setlist)
            userData.setlists = updatedSetlists
            firestore.collection("users").document(userId).set(userData).await()
            firestore.clearPersistence().await()
            return userData
        } catch (_: Exception) {
            UserData()
        }
    }
    override suspend fun removeShow(showId: String): UserData {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
        val userId = Firebase.auth.currentUser?.uid ?: ""
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            val userData = snapshot.toObject(UserData::class.java) ?: UserData()
            val updatedSetlists = userData.setlists?.toMutableList()
            updatedSetlists?.removeIf { setlist -> setlist.id == showId }
            userData.setlists = updatedSetlists
            firestore.collection("users").document(userId).set(userData).await()
            firestore.clearPersistence().await()
            return userData
        } catch (_: Exception) {
            UserData()
        }
    }
}

class FakeFirebaseApiService @Inject constructor() : FirebaseApiService {
    override suspend fun getUser(): UserData {
        return FakeFirebaseDataSource.user
    }
    override suspend fun saveShow(setlist: Setlist): UserData {
        return FakeFirebaseDataSource.user
    }
    override suspend fun removeShow(showId: String): UserData {
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
