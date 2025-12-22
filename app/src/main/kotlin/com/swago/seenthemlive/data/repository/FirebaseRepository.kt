package com.swago.seenthemlive.data.repository

import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Profile
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.network.FirebaseApiService
import javax.inject.Inject

interface FirebaseRepository {
    suspend fun getShows(): List<Show>
    suspend fun getArtists(): List<Artist>
    suspend fun getArtist(artistId: String): Artist
    suspend fun getShowsForArtist(artistId: String): List<GroupedShow>
    suspend fun getTracksForArtist(artistId: String): List<Track>
    suspend fun getShow(showId: String): Show
    suspend fun getTracksForShow(showId: String): List<Track>
    suspend fun getEncoreTracksForShow(showId: String): List<Track>
    suspend fun getLinkedShows(showId: String): List<Show>
    suspend fun getProfile(): Profile
    suspend fun getTopArtistsForProfile(): List<Artist>
}

class NetworkFirebaseRepository @Inject constructor(
    private val firebaseApiService: FirebaseApiService
) : FirebaseRepository {
    override suspend fun getShows(): List<Show> = firebaseApiService.getShows()
    override suspend fun getArtists(): List<Artist> = firebaseApiService.getArtists()
    override suspend fun getArtist(artistId: String): Artist = firebaseApiService.getArtist(artistId)
    override suspend fun getShowsForArtist(artistId: String): List<GroupedShow> = firebaseApiService.getShowsForArtist(artistId)
    override suspend fun getTracksForArtist(artistId: String): List<Track> = firebaseApiService.getTracksForArtist(artistId)
    override suspend fun getShow(showId: String): Show = firebaseApiService.getShow(showId)
    override suspend fun getTracksForShow(showId: String): List<Track> = firebaseApiService.getTracksForShow(showId)
    override suspend fun getEncoreTracksForShow(showId: String): List<Track> = firebaseApiService.getEncoreTracksForShow(showId)
    override suspend fun getLinkedShows(showId: String): List<Show> = firebaseApiService.getLinkedShows(showId)
    override suspend fun getProfile(): Profile = firebaseApiService.getProfile()
    override suspend fun getTopArtistsForProfile(): List<Artist> = firebaseApiService.getTopArtistsForProfile()
}
