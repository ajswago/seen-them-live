package com.swago.seenthemlive.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.swago.seenthemlive.R
import com.swago.seenthemlive.api.setlistfm.Setlist
import kotlinx.android.synthetic.main.activity_setlist_detail.*

class SetlistDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setlist_detail)
//        setSupportActionBar(findViewById(R.id.toolbar))
        val setlist: Setlist = intent.getSerializableExtra(INTENT_SETLIST) as Setlist
        setlist_detail_artist?.text = setlist.artist?.name
        var venueString = StringBuilder()
        venueString.append(setlist.venue?.name)
        setlist.venue?.city.let {
            venueString.append(" (")
            venueString.append(setlist.venue?.city?.name)
            setlist.venue?.city?.state.let {
                venueString.append(", ")
                venueString.append(setlist.venue?.city?.state)
            }
            setlist.venue?.city?.country?.name.let {
                venueString.append(", ")
                venueString.append(setlist.venue?.city?.country?.name)
            }
            venueString.append(")")
        }
        setlist_detail_venue?.text = venueString.toString()
        setlist_detail_tour?.text = setlist.tour?.name
        setlist_detail_date?.text = setlist.eventDate

        Log.d("Setlist", "${setlist}")
        val songs = ArrayList<SongItem>()
        val encoreSongs = ArrayList<SongItem>()
        setlist.sets?.set?.forEach { set ->
            set.song?.map { song ->
                val songItem = SongItem(set.song?.indexOf(song) + 1, song.name)
                if (set.encore == 1)
                    encoreSongs.add(songItem)
                else
                    songs.add(songItem)
            }
        }

        song_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@SetlistDetailActivity)
            adapter = SongListAdapter(songs)
        }

        encore_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@SetlistDetailActivity)
            adapter = SongListAdapter(encoreSongs)
        }

        Log.d("Songs", "${songs}")
        Log.d("Encore", "${encoreSongs}")
//        song_recycler_view.adapter = SongListAdapter(songs)
//        encore_recycler_view.adapter = SongListAdapter(encoreSongs)
    }

    companion object {

        private val INTENT_SETLIST = "setlist"

        fun newIntent(context: Context,
                      setlist: Setlist): Intent {
            val intent = Intent(context, SetlistDetailActivity::class.java)
            intent.putExtra(INTENT_SETLIST, setlist)
            return intent
        }
    }
}