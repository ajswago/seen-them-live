package com.swago.seenthemlive.ui.artist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swago.seenthemlive.R

class ArtistDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_detail_activity)
        if (savedInstanceState == null) {
            val artistId: String = intent.getStringExtra(INTENT_ARTIST_ID) ?: ""
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ArtistDetailFragment.newInstance(artistId))
                .commitNow()
        }
    }

    companion object {
        private const val INTENT_ARTIST_ID = "artistId"

        fun newIntent(context: Context, artistId: String) : Intent {
            val intent = Intent(context, ArtistDetailActivity::class.java)
            intent.putExtra(INTENT_ARTIST_ID, artistId)
            return intent
        }
    }
}