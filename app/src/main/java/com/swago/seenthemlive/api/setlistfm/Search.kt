package com.swago.seenthemlive.api.setlistfm

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable

interface Search{
    @GET("search/setlists")
    fun getSetlists(@Query("artistMbid") artistMbid: String? = null,
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
    val artist: Artist?,
    val venue: Venue?,
    val tour: Tour?,
    val sets: Sets?,
    val info: String?,
    val url: String?,
    val id: String?,
    val versionId: String?,
    val eventDate: String?,
    val lastUpdated: String?
) : Serializable

data class Artist(
    val mbid: String?,
    val tmid: Int?,
    val name: String?,
    val sortName: String?,
    val disambiguation: String?,
    val url: String?
) : Serializable

data class Venue(
    val city: City?,
    val url: String?,
    val id: String?,
    val name: String?
) : Serializable

data class City(
    val id: String?,
    val name: String?,
    val stateCode: String?,
    val state: String?,
    val coords: Coords?,
    val country: Country?
) : Serializable

data class Coords(
    val long: Double?,
    val lat: Double?
) : Serializable

data class Country(
    val code: String?,
    val name: String?
) : Serializable

data class Tour(
    val name: String?
) : Serializable

data class Sets(
    val set: List<Set>?
) : Serializable

data class Set(
    val name: String?,
    val encore: Int?,
    val song: List<Song>?
) : Serializable

data class Song(
    val name: String?,
    val with: Artist?,
    val cover: Artist?,
    val info: String?,
    val tape: Boolean?
) : Serializable

data class SetlistResponse(
    val setlist: List<Setlist>
)
