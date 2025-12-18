package com.swago.seenthemlive.data.repository

import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.network.FirebaseApiService
import javax.inject.Inject

interface FirebaseRepository {
    suspend fun getShows(): List<Show>
}

class NetworkFirebaseRepository @Inject constructor(
    private val firebaseApiService: FirebaseApiService
) : FirebaseRepository {
    override suspend fun getShows(): List<Show> = firebaseApiService.getShows()
}
