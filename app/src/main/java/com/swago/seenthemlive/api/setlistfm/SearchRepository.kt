package com.swago.seenthemlive.api.setlistfm

import com.swago.seenthemlive.api.BaseRepository

class SearchRepository(private val api : Search) : BaseRepository() {

    suspend fun getSetlists(artistMbid: String? = null,
                                 artistName: String? = null,
                                 artistTmid: Int? = null,
                                 cityId: String? = null,
                                 cityName: String? = null,
                                 countryCode: String? = null,
                                 date: String? = null,
                                 lastFm: Int? = null,
                                 lastUpdated: String? = null,
                                 p: Int? = null,
                                 state: String? = null,
                                 stateCode: String? = null,
                                 tourName: String? = null,
                                 venueId: String? = null,
                                 venueName: String? = null,
                                 year: String? = null) : MutableList<Setlist>?{

        val setlistResponse = safeApiCall(
            call = {api.getSetlistsAsync(
                artistMbid,
                artistName,
                artistTmid,
                cityId,
                cityName,
                countryCode,
                date,
                lastFm,
                lastUpdated,
                p,
                state,
                stateCode,
                tourName,
                venueId,
                venueName,
                year).await()},
            errorMessage = "Error Fetching Setlists"
        )

        return setlistResponse?.setlist?.toMutableList()

    }

}