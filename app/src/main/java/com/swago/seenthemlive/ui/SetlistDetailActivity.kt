package com.swago.seenthemlive.ui

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.swago.seenthemlive.LoginActivity
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ViewConcertsActivity
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.ui.search.SearchResultActivity
import kotlinx.android.synthetic.main.activity_setlist_detail.*

class SetlistDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setlist_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        val userId = intent.getStringExtra(INTENT_USER)
        val setlist: Setlist = intent.getSerializableExtra(INTENT_SETLIST) as Setlist

        save_setlist_checkbox.isEnabled = false

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId!!)
            .get()
            .addOnSuccessListener { documentReference ->
                val user: LoginActivity.User = documentReference.toObject(LoginActivity.User::class.java)!!
                if (user?.setlists == null) {
                    user?.setlists = ArrayList()
                }
                Log.d("CLOUDFIRESTORE", "DocumentSnapshot retrieved with ID: ${user}")
                val isSaved = user.setlists?.filter{ setlistItem -> setlistItem.id == setlist.id }?.isNotEmpty()
                Log.d("Is Saved", "Is Saved: ${isSaved}")
                isSaved.let{
                    save_setlist_checkbox.isChecked = isSaved!!
                }

                save_setlist_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    Log.d("Star", "Checked: ${isChecked}")
                    val savedSetlists = ArrayList<Setlist>()
                    savedSetlists.addAll(user.setlists!!)
                    if (isChecked) {
                        savedSetlists.add(setlist)
                        user.setlists = savedSetlists
                    } else {
                        savedSetlists.removeAll { setlistItem -> setlist.id == setlistItem.id }
                        user.setlists = savedSetlists
                    }
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(user.id!!)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d("CLOUDFIRESTORE", "User Saved added with ID: ${documentReference}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("CLOUDFIRESTORE", "Error adding document", e)
                        }
                }
                save_setlist_checkbox.isEnabled = true
            }
            .addOnFailureListener { e ->
                Log.w("CLOUDFIRESTORE", "Error adding document", e)
                save_setlist_checkbox.isEnabled = true
            }

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

        setlist_label_encore.visibility = if (encoreSongs.isEmpty()) View.GONE else View.VISIBLE

        encore_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@SetlistDetailActivity)
            adapter = SongListAdapter(encoreSongs)
        }

        other_artist_button.setOnClickListener() {
            val intent = SearchResultActivity.newIntent(this, userId, venueId = setlist.venue?.id, date = setlist.eventDate, excludeArtistMbid = setlist.artist?.mbid)
            startActivity(intent)
        }

//        Log.d("Saved User Setlists", "Setlists: ${user.setlists}")
//        Log.d("Current Setlist", "Setlist: ${setlist}")

//        val isSaved = user.setlists?.filter{ setlistItem -> setlistItem.id == setlist.id }?.isNotEmpty()
//        Log.d("Is Saved", "Is Saved: ${isSaved}")
//        isSaved.let{
//            save_setlist_checkbox.isChecked = isSaved!!
//        }
//
//        save_setlist_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            Log.d("Star", "Checked: ${isChecked}")
//            val savedSetlists = ArrayList<Setlist>()
//            savedSetlists.addAll(user.setlists!!)
//            if (isChecked) {
//                savedSetlists.add(setlist)
//                user.setlists = savedSetlists
//            } else {
//                savedSetlists.removeAll { setlistItem -> setlist.id == setlistItem.id }
//                user.setlists = savedSetlists
//            }
//            val db = FirebaseFirestore.getInstance()
//            db.collection("users").document(user.id!!)
//                .set(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d("CLOUDFIRESTORE", "User Saved added with ID: ${documentReference}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w("CLOUDFIRESTORE", "Error adding document", e)
//                }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setlist_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                Log.d("DetailActivity", "ADD THIS SETLIST")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private val INTENT_SETLIST = "setlist"
        private val INTENT_USER = "user"

        fun newIntent(context: Context,
                      userId: String,
                      setlist: Setlist): Intent {
            val intent = Intent(context, SetlistDetailActivity::class.java)
            intent.putExtra(INTENT_USER, userId)
            intent.putExtra(INTENT_SETLIST, setlist)
            return intent
        }
    }
}