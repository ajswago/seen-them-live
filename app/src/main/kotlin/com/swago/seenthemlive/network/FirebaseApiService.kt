package com.swago.seenthemlive.network

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

interface FirebaseApiService {
    suspend fun getUser(): UserData
    suspend fun saveShow(setlist: Setlist): UserData
    suspend fun removeShow(showId: String): UserData
}

class NetworkFirebaseApiService @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseApiService {
    
    // Mutex to prevent concurrent migration attempts
    private val migrationMutex = Mutex()
    
    // User-safe in-memory cache to prevent redundant fetches
    private var cachedUserData: UserData? = null
    private var cachedUserId: String? = null

    override suspend fun getUser(): UserData {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            cachedUserData = null
            cachedUserId = null
            return UserData()
        }
        
        // Return cache if available and matches current user
        if (userId == cachedUserId) {
            cachedUserData?.let { return it }
        } else {
            cachedUserData = null
            cachedUserId = null
        }

        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            val userData = snapshot.toObject(UserData::class.java) ?: UserData()

            // Fetch the subcollection setlists
            val subcollectionSnapshot = firestore.collection("users")
                .document(userId)
                .collection("setlists")
                .get()
                .await()
            val subcollectionSetlists = subcollectionSnapshot.toObjects(Setlist::class.java)

            // Thread-safe migration check
            migrationMutex.withLock {
                // Double-check cache inside the lock
                if (userId == cachedUserId) {
                    cachedUserData?.let { return it }
                }

                if (snapshot.contains("setlists")) {
                    val oldSetlists = userData.setlists
                    if (oldSetlists != null && oldSetlists.isNotEmpty()) {
                        // Chunk setlists to respect Firestore's 500-write limit per batch
                        val batchSize = 400
                        val chunks = oldSetlists.chunked(batchSize)
                        for (chunk in chunks) {
                            val batch = firestore.batch()
                            for (setlist in chunk) {
                                val setlistId = setlist.id ?: continue
                                val docRef = firestore.collection("users")
                                    .document(userId)
                                    .collection("setlists")
                                    .document(setlistId)
                                batch.set(docRef, setlist)
                            }
                            batch.commit().await()
                        }
                        
                        // Clean up: delete the old nested array from the root document
                        firestore.collection("users").document(userId)
                            .update("setlists", FieldValue.delete())
                            .await()

                        userData.setlists = oldSetlists
                    } else {
                        // Clean up empty nested field
                        firestore.collection("users").document(userId)
                            .update("setlists", FieldValue.delete())
                            .await()
                        userData.setlists = subcollectionSetlists
                    }
                } else {
                    userData.setlists = subcollectionSetlists
                }
            }
            
            cachedUserData = userData
            cachedUserId = userId
            userData
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            throw e 
        }
    }
    
    override suspend fun saveShow(setlist: Setlist): UserData {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        if (userId.isEmpty()) return UserData()
        
        val setlistId = setlist.id ?: throw IllegalArgumentException("Setlist ID cannot be null")
        
        return try {
            // First retrieve and migrate user profile (if cachedUserData is null or different user)
            val userData = getUser()
            
            // Save the new setlist to the subcollection
            firestore.collection("users")
                .document(userId)
                .collection("setlists")
                .document(setlistId)
                .set(setlist)
                .await()
                
            // Update the local list and cache
            val updatedSetlists = userData.setlists?.toMutableList() ?: mutableListOf()
            updatedSetlists.removeIf { it.id == setlistId }
            updatedSetlists.add(setlist)
            userData.setlists = updatedSetlists
            
            cachedUserData = userData
            cachedUserId = userId
            
            firestore.clearPersistence().await()
            userData
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun removeShow(showId: String): UserData {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        if (userId.isEmpty()) return UserData()
        
        return try {
            // First retrieve and migrate user profile (if cachedUserData is null or different user)
            val userData = getUser()
            
            // Delete the setlist from the subcollection
            firestore.collection("users")
                .document(userId)
                .collection("setlists")
                .document(showId)
                .delete()
                .await()
                
            // Update the local list and cache
            val updatedSetlists = userData.setlists?.toMutableList() ?: mutableListOf()
            updatedSetlists.removeIf { it.id == showId }
            userData.setlists = updatedSetlists
            
            cachedUserData = userData
            cachedUserId = userId
            
            firestore.clearPersistence().await()
            userData
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            throw e
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
