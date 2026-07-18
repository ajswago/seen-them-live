package com.swago.seenthemlive.network

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
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
        if (userId.isEmpty()) return UserData()
        
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            val userData = snapshot.toObject(UserData::class.java) ?: UserData()
            
            // Query the setlists subcollection
            val subcollectionSnapshot = firestore.collection("users")
                .document(userId)
                .collection("setlists")
                .get()
                .await()
            val subcollectionSetlists = subcollectionSnapshot.toObjects(Setlist::class.java)
            
            // Check if the old nested setlists list exists in the root document
            if (snapshot.contains("setlists")) {
                val oldSetlists = userData.setlists
                if (oldSetlists != null && oldSetlists.isNotEmpty()) {
                    val batch = firestore.batch()
                    for (setlist in oldSetlists) {
                        val setlistId = setlist.id ?: continue
                        val docRef = firestore.collection("users")
                            .document(userId)
                            .collection("setlists")
                            .document(setlistId)
                        batch.set(docRef, setlist)
                    }
                    // Delete the old nested 'setlists' field from the root document
                    val userDocRef = firestore.collection("users").document(userId)
                    batch.update(userDocRef, "setlists", FieldValue.delete())
                    batch.commit().await()
                    
                    userData.setlists = oldSetlists
                } else {
                    // It has the field but it is empty/null, let's remove the field to clean up
                    firestore.collection("users").document(userId)
                        .update("setlists", FieldValue.delete())
                        .await()
                    userData.setlists = subcollectionSetlists
                }
            } else {
                userData.setlists = subcollectionSetlists
            }
            userData
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
        if (userId.isEmpty()) return UserData()
        
        return try {
            // First retrieve and migrate user profile
            val userData = getUser()
            
            // Save the new setlist to the subcollection
            val setlistId = setlist.id ?: throw IllegalArgumentException("Setlist ID cannot be null")
            firestore.collection("users")
                .document(userId)
                .collection("setlists")
                .document(setlistId)
                .set(setlist)
                .await()
                
            // Update the local list
            val updatedSetlists = userData.setlists?.toMutableList() ?: mutableListOf()
            updatedSetlists.removeIf { it.id == setlistId }
            updatedSetlists.add(setlist)
            userData.setlists = updatedSetlists
            
            firestore.clearPersistence().await()
            userData
        } catch (ce: CancellationException) {
            throw ce
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
        if (userId.isEmpty()) return UserData()
        
        return try {
            // First retrieve and migrate user profile
            val userData = getUser()
            
            // Delete the setlist from the subcollection
            firestore.collection("users")
                .document(userId)
                .collection("setlists")
                .document(showId)
                .delete()
                .await()
                
            // Update the local list
            val updatedSetlists = userData.setlists?.toMutableList() ?: mutableListOf()
            updatedSetlists.removeIf { it.id == showId }
            userData.setlists = updatedSetlists
            
            firestore.clearPersistence().await()
            userData
        } catch (ce: CancellationException) {
            throw ce
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
