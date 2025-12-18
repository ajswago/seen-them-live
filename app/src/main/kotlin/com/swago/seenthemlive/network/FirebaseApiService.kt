package com.swago.seenthemlive.network

import com.swago.seenthemlive.models.Show
import javax.inject.Inject

interface FirebaseApiService {

    suspend fun getShows(): List<Show>
}

class NetworkFirebaseApiService : FirebaseApiService {
    override suspend fun getShows(): List<Show> {
        return listOf<Show>()
    }
}

class FakeFirebaseApiService @Inject constructor() : FirebaseApiService {
    override suspend fun getShows(): List<Show> {
        return FakeFirebaseDataSource.shows
    }
}
