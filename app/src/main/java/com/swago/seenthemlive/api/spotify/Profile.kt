package com.swago.seenthemlive.api.spotify

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface Profile {

    @GET("me")
    fun getProfile(): Deferred<Response<ProfileResponse>>
}

data class ProfileResponse(
    val country: String,
    val display_name: String,
    val email: String,
    val id: String

)
