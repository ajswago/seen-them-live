package com.swago.seenthemlive.api.spotify

import com.swago.seenthemlive.api.BaseRepository

class PlaylistRepository(private val api : Playlist) : BaseRepository() {

    suspend fun searchSong(name: String, artist: String) : SongSearchResponse? {
        return safeApiCall(call = {api.searchSong(q="artist:$artist track:$name", type=arrayOf("track"), limit = 1).await()}, errorMessage = "Error searching song on Spotify")
    }

    suspend fun createPlaylist(userId: String, name: String) : CreatePlaylistResponse? {
        return safeApiCall(call = {api.createPlaylist(userId = userId,
            body = mapOf(
            "name" to name
        )).await()}, errorMessage = "Error creating playlist on Spotify")
    }

    suspend fun addSongsToPlaylist(playlistId: String, songs: List<String>) : AddSongsResponse? {
        return safeApiCall(call = {api.addSongsToPlaylist(playlistId, mapOf("uris" to songs.toTypedArray())).await()}, errorMessage = "Error adding songs to playlist on Spotify")
    }
}