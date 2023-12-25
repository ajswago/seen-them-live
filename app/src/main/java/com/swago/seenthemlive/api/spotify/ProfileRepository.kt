package com.swago.seenthemlive.api.spotify

import com.swago.seenthemlive.api.BaseRepository

class ProfileRepository(private val api : Profile) : BaseRepository() {

    suspend fun getProfile() : ProfileResponse? {
        return safeApiCall(call = {api.getProfile().await()}, errorMessage = "Error fetching Spotify user profile")
    }
}