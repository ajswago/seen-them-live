package com.swago.seenthemlive.data.repository

import com.swago.seenthemlive.network.SpotifyApiService
import javax.inject.Inject

interface SpotifyRepository {
    suspend fun searchSong(artist: String, name: String): String?
    suspend fun createPlaylist(name: String): String?
    suspend fun addSongsToPlaylist(playlistId: String, songIds: List<String>): String
}

class NetworkSpotifyRepository @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : SpotifyRepository {
    override suspend fun searchSong(artist: String, name: String): String? =
        spotifyApiService.searchSong(artist = artist, name = name)

    override suspend fun createPlaylist(name: String): String? =
        spotifyApiService.createPlaylist(name = name)

    override suspend fun addSongsToPlaylist(playlistId: String, songIds: List<String>): String =
        spotifyApiService.addSongsToPlaylist(playlistId = playlistId, songIds = songIds)
}
