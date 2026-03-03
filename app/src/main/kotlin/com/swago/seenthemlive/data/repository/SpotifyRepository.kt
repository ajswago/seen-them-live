package com.swago.seenthemlive.data.repository

import com.swago.seenthemlive.network.SpotifyApiService
import com.swago.seenthemlive.network.SpotifyProfile
import javax.inject.Inject

interface SpotifyRepository {
    suspend fun getProfile(code: String): SpotifyProfile?
    suspend fun searchSong(code: String, artist: String, name: String): String?
    suspend fun createPlaylist(code: String, userId: String, name: String): String?
    suspend fun addSongsToPlaylist(code: String, playlistId: String, songIds: List<String>): List<String>?
}

class NetworkSpotifyRepository @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : SpotifyRepository {
    override suspend fun getProfile(code: String): SpotifyProfile? =
        spotifyApiService.getProfile(code)

    override suspend fun searchSong(code: String, artist: String, name: String): String? =
        spotifyApiService.searchSong(code = code, artist = artist, name = name)

    override suspend fun createPlaylist(code: String, userId: String, name: String): String? =
        spotifyApiService.createPlaylist(code = code, userId = userId, name = name)

    override suspend fun addSongsToPlaylist(code: String, playlistId: String, songIds: List<String>): List<String>? =
        spotifyApiService.addSongsToPlaylist(code = code, playlistId = playlistId, songIds = songIds)
}
