package com.swago.seenthemlive.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface SpotifyApiService {
    suspend fun getProfile(code: String): SpotifyProfile?
    suspend fun searchSong(code: String, artist: String, name: String): String?
    suspend fun createPlaylist(code: String, userId: String, name: String): String?
    suspend fun addSongsToPlaylist(code: String, playlistId: String, songIds: List<String>): List<String>?
}

class SpotifyAuthInterceptor(val code: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("authorization", "Bearer $code")
            .build()
        return chain.proceed(newRequest)
    }
}

class NetworkSpotifyApiService @Inject constructor() : SpotifyApiService {
    private val baseUrl = "https://api.spotify.com/v1/"

    private fun spotifyClient(code: String) = OkHttpClient().newBuilder()
        .addInterceptor(SpotifyAuthInterceptor(code))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun retrofit(code: String) : Retrofit = Retrofit.Builder()
        .client(spotifyClient(code))
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    fun profile(code: String) : Profile = retrofit(code).create(Profile::class.java)
    fun playlist(code: String) : Playlist = retrofit(code).create(Playlist::class.java)

    override suspend fun getProfile(code: String): SpotifyProfile? {
        return try {
            profile(code).getProfile()
        } catch (_ : HttpException) {
            null
        }
    }

    override suspend fun searchSong(code: String, artist: String, name: String): String? {
        return try {
            playlist(code).searchSong(
                q = "artist:$artist track:$name",
                type=arrayOf("track"),
                limit = 1
            ).tracks.items?.firstOrNull()?.uri
        } catch (_ : HttpException) {
            null
        }
    }

    override suspend fun createPlaylist(code: String, userId: String, name: String): String? {
        return try {
            playlist(code).createPlaylist(
                userId = userId,
                body = mapOf(
                    "name" to name
                )
            ).id
        } catch (_ : HttpException) {
            null
        }
    }

    override suspend fun addSongsToPlaylist(code: String, playlistId: String, songIds: List<String>): List<String>? {
        return try {
            songIds.chunked(100).mapNotNull { uriGroup ->
                playlist(code).addSongsToPlaylist(playlistId, UriBody(uris = uriGroup.toTypedArray())).snapshotId
            }
        } catch (_ : HttpException) {
            null
        }
    }
}

interface Profile {
    @GET("me")
    suspend fun getProfile(): SpotifyProfile?
}

interface Playlist {

    @GET("search")
    suspend fun searchSong(
        @Query("q") q: String?,
        @Query("type") type: Array<String>?,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null): SongSearchResponse

    @POST("users/{userId}/playlists")
    suspend fun createPlaylist(
        @Path("userId") userId: String,
        @Body body: Map<String, String>
    ): CreatePlaylistResponse

    @POST("playlists/{playlistId}/tracks")
    suspend fun addSongsToPlaylist(
        @Path("playlistId") playlistId: String,
        @Body body: UriBody
    ): AddSongsResponse
}

class FakeSpotifyApiService @Inject constructor() : SpotifyApiService {
    override suspend fun getProfile(code: String): SpotifyProfile {
        return SpotifyProfile(country = "US", displayName = "test", email = "test@test.com", id = "ID")
    }

    override suspend fun searchSong(code: String, artist: String, name: String): String {
        return "1"
    }

    override suspend fun createPlaylist(code: String, userId: String, name: String): String {
        return "1"
    }

    override suspend fun addSongsToPlaylist(code: String, playlistId: String, songIds: List<String>): List<String> {
        return listOf("1")
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class SpotifyProfile(
    val country: String,
    @SerialName("display_name") val displayName: String,
    val email: String,
    val id: String

)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class AddSongsResponse(
    @SerialName("snapshot_id") val snapshotId: String?
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CreatePlaylistResponse(
    val description: String?,
    val id: String?,
    val href: String?,
    val name: String?
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class SongSearchResponse(
    val tracks: Tracks
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Tracks(
    val items: Array<TrackItem>?
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class TrackItem(
    val name: String?,
    val uri: String?
)

@Serializable
data class UriBody(
    val uris: Array<String>
)
