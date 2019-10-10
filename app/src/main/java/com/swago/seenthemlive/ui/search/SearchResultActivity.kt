package com.swago.seenthemlive.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.swago.seenthemlive.R

class SearchResultActivity : AppCompatActivity() {

    private lateinit var searchResultViewModel: SearchResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        searchResultViewModel = ViewModelProviders.of(this).get(SearchResultViewModel::class.java)

        val artistName = intent.getStringExtra(INTENT_ARTIST_NAME)
//        Toast.makeText(this,"Artist:"+artistName, Toast.LENGTH_LONG).show()

        Log.d("SEARCHRESULT", "Making call!!!")

        searchResultViewModel.fetchSetlists(artistName = artistName)

        searchResultViewModel.setlistsLiveData.observe(this, Observer {

            Log.d("SEARCHRESULT", "Data Received!!!")
            Log.d("DATA", "Artist: ${it[0].artist.name}, Venue: ${it[0].venue.name}, Date: ${it[0].eventDate}")
            Log.d("DATA", "Artist: ${it[1].artist.name}, Venue: ${it[1].venue.name}, Date: ${it[1].eventDate}")
        })
    }

    companion object {

        private val INTENT_ARTIST_MBID = "artistMbid"
        private val INTENT_ARTIST_NAME = "artistName"
        private val INTENT_ARTIST_TMID = "artistTmid"
        private val INTENT_CITY_ID = "cityId"
        private val INTENT_CITY_NAME = "cityName"
        private val INTENT_COUNTRY_CODE = "countryCode"
        private val INTENT_DATE = "date"
        private val INTENT_LAST_FM = "lastFm"
        private val INTENT_LAST_UPDATED = "lastUpdated"
        private val INTENT_P = "p"
        private val INTENT_STATE = "state"
        private val INTENT_STATE_CODE = "stateCode"
        private val INTENT_TOUR_NAME = "tourName"
        private val INTENT_VENUE_ID = "venueId"
        private val INTENT_VENUE_NAME = "venueName"
        private val INTENT_YEAR = "year"

        fun newIntent(context: Context,
                      artistMbid: String? = null,
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
                      year: String? = null): Intent {
            val intent = Intent(context, SearchResultActivity::class.java)
            intent.putExtra(INTENT_ARTIST_MBID, artistMbid)
            intent.putExtra(INTENT_ARTIST_NAME, artistName)
            intent.putExtra(INTENT_ARTIST_TMID, artistTmid)
            intent.putExtra(INTENT_CITY_ID, cityId)
            intent.putExtra(INTENT_CITY_NAME, cityName)
            intent.putExtra(INTENT_COUNTRY_CODE, countryCode)
            intent.putExtra(INTENT_DATE, date)
            intent.putExtra(INTENT_LAST_FM, lastFm)
            intent.putExtra(INTENT_LAST_UPDATED, lastUpdated)
            intent.putExtra(INTENT_P, p)
            intent.putExtra(INTENT_STATE, state)
            intent.putExtra(INTENT_STATE_CODE, stateCode)
            intent.putExtra(INTENT_TOUR_NAME, tourName)
            intent.putExtra(INTENT_VENUE_ID, venueId)
            intent.putExtra(INTENT_VENUE_NAME, venueName)
            intent.putExtra(INTENT_YEAR, year)
            return intent
        }
    }
}
