package com.swago.seenthemlive.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.swago.seenthemlive.BuildConfig
import com.swago.seenthemlive.ui.components.cards.SearchTerms
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.serialization.Serializable
import okhttp3.Response
import retrofit2.HttpException
import kotlin.collections.listOf

interface SetlistFmApiService {
    suspend fun getSearchResults(searchTerms: SearchTerms): SetlistResponse
    suspend fun getSearchResults(date: Date, venue: String): SetlistResponse
    suspend fun getSetlist(id: String): Setlist
}

class SetlistFmAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("x-api-key", BuildConfig.SETLISTFM_API_KEY)
            .header("Accept", "application/json")
            .build()
        return chain.proceed(newRequest)
    }
}

class NetworkSetlistFmApiService @Inject constructor() : SetlistFmApiService {

    private val baseUrl = "https://api.setlist.fm/rest/1.0/"

    private val setlistfmClient = OkHttpClient().newBuilder()
        .addInterceptor(SetlistFmAuthInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(setlistfmClient)
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    val search : Search = retrofit().create(Search::class.java)

    val getSetlist : GetSetlist = retrofit().create(GetSetlist::class.java)

    override suspend fun getSearchResults(searchTerms: SearchTerms): SetlistResponse {
        return try {
            search.getSetlists(
                artistName = searchTerms.artist,
                venueName = searchTerms.venue,
                stateCode = searchTerms.usState
            )
        } catch (_ : HttpException) {
            SetlistResponse(setlist = listOf())
        }
    }

    override suspend fun getSearchResults(date: Date, venue: String): SetlistResponse {
        try {
            val dateString = SimpleDateFormat(
                "dd-MM-yyyy", Locale.US
            ).format(date)
            return search.getSetlists(
                date = dateString,
                venueName = venue,
            )
        } catch (_ : HttpException) {
            return SetlistResponse(setlist = listOf())
        }
    }

    override suspend fun getSetlist(id: String): Setlist {
        return getSetlist.getSetlist(id)
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

interface Search {
    @GET("search/setlists")
    suspend fun getSetlists(@Query("artistMbid") artistMbid: String? = null,
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
                         @Query("year") year: String? = null): SetlistResponse
}

interface GetSetlist {
    @GET("setlist/{setlistId}")
    suspend fun getSetlist(@Path("setlistId") setlistId: String): Setlist
}

@Serializable
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
)

@Serializable
data class Artist(
    val mbid: String? = null,
    val tmid: Int? = null,
    val name: String? = null,
    val sortName: String? = null,
    val disambiguation: String? = null,
    val url: String? = null
)

@Serializable
data class Venue(
    val city: City? = null,
    val url: String? = null,
    val id: String? = null,
    val name: String? = null
)

@Serializable
data class City(
    val id: String? = null,
    val name: String? = null,
    val stateCode: String? = null,
    val state: String? = null,
    val coords: Coords? = null,
    val country: Country? = null
)

@Serializable
data class Coords(
    val long: Double? = null,
    val lat: Double? = null
)

@Serializable
data class Country(
    val code: String? = null,
    val name: String? = null
)

@Serializable
data class Tour(
    val name: String? = null
)

@Serializable
data class Sets(
    val set: List<Set>? = null
)

@Serializable
data class Set(
    val name: String? = null,
    val encore: Int? = null,
    val song: List<Song>? = null
)

@Serializable
data class Song(
    val name: String? = null,
    val with: Artist? = null,
    val cover: Artist? = null,
    val info: String? = null,
    val tape: Boolean? = null
)

@Serializable
data class SetlistResponse(
    val type: String = "",
    val itemsPerPage: Int = 0,
    val page: Int = 0,
    val total: Int = 0,
    val setlist: List<Setlist>
)
