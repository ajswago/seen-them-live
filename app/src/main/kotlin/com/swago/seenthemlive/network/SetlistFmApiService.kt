package com.swago.seenthemlive.network

import com.swago.seenthemlive.ui.components.cards.SearchTerms
import java.io.Serializable
import java.util.Date
import javax.inject.Inject

interface SetlistFmApiService {
    suspend fun getSearchResults(searchTerms: SearchTerms): SetlistResponse
    suspend fun getSearchResults(date: Date, venue: String): SetlistResponse
    suspend fun getSetlist(id: String): Setlist
}

class NetworkSetlistFmApiService : SetlistFmApiService {
    override suspend fun getSearchResults(searchTerms: SearchTerms): SetlistResponse {
        return SetlistResponse(listOf())
    }

    override suspend fun getSearchResults(date: Date, venue: String): SetlistResponse {
        return SetlistResponse(listOf())
    }

    override suspend fun getSetlist(id: String): Setlist {
        return Setlist()
    }
}

class FakeSetlistFmApiService @Inject constructor() : SetlistFmApiService {
    override suspend fun getSearchResults(searchTerms: SearchTerms): SetlistResponse {
        return FakeSetlistFmDataSource.setlistResponse
    }

    override suspend fun getSearchResults(date: Date, venue: String): SetlistResponse {
        return FakeSetlistFmDataSource.relatedShowsResponse
    }

    override suspend fun getSetlist(id: String): Setlist {
        return FakeSetlistFmDataSource.setlistResponse.setlist.first { it.id == id }
    }
}

data class Setlist(
    val artist: Artist? = null,
    val venue: Venue? = null,
    val tour: Tour? = null,
    val sets: Sets? = null,
    val info: String? = null,
    val url: String? = null,
    val id: String? = null,
    val versionId: String? = null,
    val eventDate: String? = null,
    val lastUpdated: String? = null
) : Serializable

data class Artist(
    val mbid: String? = null,
    val tmid: Int? = null,
    val name: String? = null,
    val sortName: String? = null,
    val disambiguation: String? = null,
    val url: String? = null
) : Serializable

data class Venue(
    val city: City? = null,
    val url: String? = null,
    val id: String? = null,
    val name: String? = null
) : Serializable

data class City(
    val id: String? = null,
    val name: String? = null,
    val stateCode: String? = null,
    val state: String? = null,
    val coords: Coords? = null,
    val country: Country? = null
) : Serializable

data class Coords(
    val long: Double? = null,
    val lat: Double? = null
) : Serializable

data class Country(
    val code: String? = null,
    val name: String? = null
) : Serializable

data class Tour(
    val name: String? = null
) : Serializable

data class Sets(
    val set: List<Set>? = null
) : Serializable

data class Set(
    val name: String? = null,
    val encore: Int? = null,
    val song: List<Song>? = null
) : Serializable

data class Song(
    val name: String? = null,
    val with: Artist? = null,
    val cover: Artist? = null,
    val info: String? = null,
    val tape: Boolean? = null
) : Serializable

data class SetlistResponse(
    val setlist: List<Setlist>
)
