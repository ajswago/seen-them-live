package com.swago.seenthemlive.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.api.setlistfm.SearchRepository
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.api.setlistfm.SetlistfmApiBuilder
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchResultViewModel : ViewModel(){

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : SearchRepository = SearchRepository(SetlistfmApiBuilder.search)


    val setlistsLiveData = MutableLiveData<MutableList<Setlist>>()

    fun fetchSetlists(artistMbid: String? = null,
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
                      year: String? = null){
        scope.launch {
            val setlists = repository.getSetlists(
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
                year)
            setlistsLiveData.postValue(setlists)
        }
    }


    fun cancelAllRequests() = coroutineContext.cancel()

}