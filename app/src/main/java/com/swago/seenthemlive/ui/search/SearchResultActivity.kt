package com.swago.seenthemlive.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.ui.setlist.SetlistActivity
import kotlinx.android.synthetic.main.activity_search_result.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class SearchResultActivity : AppCompatActivity() {

    private val setlists = mutableListOf<Setlist>()

    private lateinit var searchResultViewModel: SearchResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.swago.seenthemlive.R.layout.activity_search_result)

        val userId = intent.getStringExtra(INTENT_USER)
        val nested = intent.getBooleanExtra(INTENT_NESTED, false)

        if (nested) {
            title = "Also At This Show"
        } else {
            title = "Search Results"
        }

        searchResultViewModel = ViewModelProvider(this).get(SearchResultViewModel::class.java)

        list_recycler_view.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(this@SearchResultActivity)
            // set the custom adapter to the RecyclerView
            adapter = SetlistListAdapter(setlists, object : SetlistListAdapter.OnSelectListener {
                override fun selected(setlist: Setlist) {
                    Log.d("SEARCH RESULT", "SELECTED SETLIST: ${setlist}")
                    val intent = SetlistActivity.newIntent(context, setlist, showOthersAtShow = !nested)
                    startActivity(intent)
                }
            })
        }

        list_recycler_view.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        val artistName = intent.getStringExtra(INTENT_ARTIST_NAME)
        val stateCode = intent.getStringExtra(INTENT_STATE_CODE)
        val venueId = intent.getStringExtra(INTENT_VENUE_ID)
        val venueName = intent.getStringExtra(INTENT_VENUE_NAME)
        val date = intent.getStringExtra(INTENT_DATE)
        val excludeArtistMbid = intent.getStringExtra(INTENT_EXCLUDE_ARTIST_MBID)
//        Toast.makeText(this,"Artist:"+artistName, Toast.LENGTH_LONG).show()

        Log.d("SEARCHRESULT", "Making call!!!")

        loading?.show()
        searchResultViewModel.fetchSetlists(artistName = artistName, stateCode = stateCode, venueId = venueId, venueName = venueName, date = date)

        searchResultViewModel.setlistsLiveData.observe(this, Observer {
            Log.d("QUERY", "Setlists found: ${it}")
            val filteredList = it.filter { setlist -> setlist.artist?.mbid != excludeArtistMbid }
            if (filteredList.isEmpty()) {
                no_content_view.visibility = View.VISIBLE
                list_recycler_view.visibility = View.GONE
                if (nested) {
                    no_content_view.text = "No Additional Artists Found!"
                } else {
                    no_content_view.text = "No Search Results Found!"
                }
            } else {
                setlists.clear()
                setlists.addAll(filteredList.sortedByDescending {
                    LocalDate.parse(
                        it.eventDate,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                    )
                })
                list_recycler_view.adapter?.notifyDataSetChanged()
                no_content_view.visibility = View.GONE
                list_recycler_view.visibility = View.VISIBLE
            }
            loading.hide()
        })
    }

    companion object {

        private val INTENT_USER = "user"
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
        private val INTENT_EXCLUDE_ARTIST_MBID = "excludeArtistMbid"
        private val INTENT_NESTED = "nested"

        fun newIntent(context: Context,
                      userId: String,
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
                      year: String? = null,
                      excludeArtistMbid: String? = null,
                      nested: Boolean? = false): Intent {
            val intent = Intent(context, SearchResultActivity::class.java)
            intent.putExtra(INTENT_USER, userId)
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
            intent.putExtra(INTENT_EXCLUDE_ARTIST_MBID, excludeArtistMbid)
            intent.putExtra(INTENT_NESTED, nested)
            return intent
        }
    }
}
