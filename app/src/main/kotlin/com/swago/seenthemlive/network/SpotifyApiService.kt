package com.swago.seenthemlive.network

import javax.inject.Inject

interface SpotifyApiService {
    suspend fun searchSong(artist: String, name: String): String?
    suspend fun createPlaylist(name: String): String?
    suspend fun addSongsToPlaylist(playlistId: String, songIds: List<String>): String
}

class NetworkSpotifyApiService : SpotifyApiService {
    override suspend fun searchSong(artist: String, name: String): String? {
        return ""
    }

    override suspend fun createPlaylist(name: String): String? {
        return ""
    }

    override suspend fun addSongsToPlaylist(playlistId: String, songIds: List<String>): String {
        return ""
    }
}

class FakeSpotifyApiService @Inject constructor() : SpotifyApiService {
    override suspend fun searchSong(artist: String, name: String): String? {
        return "1"
    }

    override suspend fun createPlaylist(name: String): String? {
        return "1"
    }

    override suspend fun addSongsToPlaylist(playlistId: String, songIds: List<String>): String {
        return "1"
    }
}
