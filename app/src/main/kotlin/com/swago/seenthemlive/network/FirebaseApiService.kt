package com.swago.seenthemlive.network

import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Profile
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import java.util.Date
import javax.inject.Inject

interface FirebaseApiService {

    suspend fun getShows(): List<Show>
    suspend fun getArtists(): List<Artist>
    suspend fun getArtist(artistId: String): Artist
    suspend fun getShowsForArtist(artistId: String): List<GroupedShow>
    suspend fun getTracksForArtist(artistId: String): List<Track>
    suspend fun getShow(showId: String): Show
    suspend fun getTracksForShow(showId: String): List<Track>
    suspend fun getLinkedShows(showId: String): List<Show>
    suspend fun getProfile(): Profile
    suspend fun getTopArtistsForProfile(): List<Artist>
}

class NetworkFirebaseApiService : FirebaseApiService {
    override suspend fun getShows(): List<Show> {
        return listOf()
    }

    override suspend fun getArtists(): List<Artist> {
        return listOf()
    }

    override suspend fun getArtist(artistId: String): Artist {
        return Artist("", "", Date())
    }

    override suspend fun getShowsForArtist(artistId: String): List<GroupedShow> {
        return listOf()
    }

    override suspend fun getTracksForArtist(artistId: String): List<Track> {
        return listOf()
    }

    override suspend fun getShow(showId: String): Show {
        return Show("", "", "", "", "", Date())
    }

    override suspend fun getTracksForShow(showId: String): List<Track> {
        return listOf()
    }

    override suspend fun getLinkedShows(showId: String): List<Show> {
        return listOf()
    }

    override suspend fun getProfile(): Profile {
        return Profile("", "", 0, 0, 0)
    }

    override suspend fun getTopArtistsForProfile(): List<Artist> {
        return listOf()
    }
}

class FakeFirebaseApiService @Inject constructor() : FirebaseApiService {
    override suspend fun getShows(): List<Show> {
        return FakeFirebaseDataSource.shows
    }

    override suspend fun getArtists(): List<Artist> {
        return FakeFirebaseDataSource.artists
    }

    override suspend fun getArtist(artistId: String): Artist {
        return FakeFirebaseDataSource.artistInfo
    }

    override suspend fun getShowsForArtist(artistId: String): List<GroupedShow> {
        return FakeFirebaseDataSource.groupedShowsForArtist
    }

    override suspend fun getTracksForArtist(artistId: String): List<Track> {
        return FakeFirebaseDataSource.tracksForArtist
    }

    override suspend fun getShow(showId: String): Show {
        return FakeFirebaseDataSource.showInfo
    }

    override suspend fun getTracksForShow(showId: String): List<Track> {
        return FakeFirebaseDataSource.tracksForShow
    }

    override suspend fun getLinkedShows(showId: String): List<Show> {
        return FakeFirebaseDataSource.relatedShowsForShow
    }

    override suspend fun getProfile(): Profile {
        return FakeFirebaseDataSource.profileInfo
    }

    override suspend fun getTopArtistsForProfile(): List<Artist> {
        return FakeFirebaseDataSource.topArtistsForProfile
    }
}
