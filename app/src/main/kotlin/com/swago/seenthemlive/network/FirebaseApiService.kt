package com.swago.seenthemlive.network

import java.io.Serializable
import javax.inject.Inject

interface FirebaseApiService {
    suspend fun getUser(): UserData
}

class NetworkFirebaseApiService : FirebaseApiService {
    override suspend fun getUser(): UserData {
        return UserData()
    }
}

class FakeFirebaseApiService @Inject constructor() : FirebaseApiService {
    override suspend fun getUser(): UserData {
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
