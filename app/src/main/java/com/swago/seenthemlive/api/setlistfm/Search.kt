package com.swago.seenthemlive.api.setlistfm

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable

interface Search{
    @GET("search/setlists")
    fun getSetlistsAsync(@Query("artistMbid") artistMbid: String? = null,
                         @Query("artistName") artistName: String? = null,
                         @Query("artistTmid") artistTmid: Int? = null,
                         @Query("cityId") cityId: String? = null,
                         @Query("cityName") cityName: String? = null,
                         @Query("countryCode") countryCode: String? = null,
                         @Query("date") date: String? = null,
                         @Query("lastFm") lastFm: Int? = null,
                         @Query("lastUpdated") lastUpdated: String? = null,
                         @Query("p") p: Int? = null,
                         @Query("state") state: String? = null,
                         @Query("stateCode") stateCode: String? = null,
                         @Query("tourName") tourName: String? = null,
                         @Query("venueId") venueId: String? = null,
                         @Query("venueName") venueName: String? = null,
                         @Query("year") year: String? = null): Deferred<Response<SetlistResponse>>
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
