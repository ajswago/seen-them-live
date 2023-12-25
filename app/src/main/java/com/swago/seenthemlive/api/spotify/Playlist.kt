package com.swago.seenthemlive.api.spotify

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Playlist {

    @GET("search")
    fun searchSong(
        @Query("q") q: String?,
        @Query("type") type: Array<String>?,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null): Deferred<Response<SongSearchResponse>>

    @POST("users/{userId}/playlists")
    fun createPlaylist(
        @Path("userId") userId: String,
        @Body body: Map<String, String>
    ): Deferred<Response<CreatePlaylistResponse>>

    @POST("playlists/{playlistId}/tracks")
    fun addSongsToPlaylist(
        @Path("playlistId") playlistId: String,
        @Body body: Map<String, Array<String>>
    ): Deferred<Response<AddSongsResponse>>
}

data class AddSongsResponse(
    val snapshotId: String?
)

data class CreatePlaylistResponse(
    val description: String?,
    val id: String?,
    val href: String?,
    val name: String?
)

data class SongSearchResponse(
    val tracks: Tracks
)

data class Tracks(
    val items: Array<TrackItem>?
)

data class TrackItem(
    val name: String?,
    val uri: String?
)