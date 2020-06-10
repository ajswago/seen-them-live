package com.swago.seenthemlive.firebase.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.swago.seenthemlive.api.setlistfm.Setlist
import kotlinx.coroutines.tasks.await
import java.io.Serializable

class UserRepository() {

    companion object {
        suspend fun getUser(userId : String)
                : UserData?{
            val firestore = Firebase.firestore
            return try{
                val data = firestore
                    .collection("users")
                    .document(userId)
                    .get()
                    .await()
                data.toObject<UserData>()
            }catch (e : Exception){
                null
            }
        }

        suspend fun setUser(user : UserData) : Boolean {
            val firestore = Firebase.firestore
            return try{
                firestore
                    .collection("users")
                    .document(user.id ?: "")
                    .set(user)
                    .await()
                true
            }catch (e : Exception){
                false
            }
        }
    }
}

data class UserData(
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var displayName: String? = null,
    var setlists: List<Setlist>? = null
) : Serializable


