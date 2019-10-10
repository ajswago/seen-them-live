package com.swago.seenthemlive.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.swago.seenthemlive.R

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val artistField: EditText = findViewById(R.id.artist_field)
        val searchButton: Button = findViewById(R.id.search_button)
        searchButton.setOnClickListener { view ->
            if (validateForm()) {
//                Toast.makeText(this,"Artist:"+artistField.text, Toast.LENGTH_LONG).show()
                val intent = SearchResultActivity.newIntent(this, artistMbid = null, artistName = artistField.text.toString())
                startActivity(intent)
            }
        }
    }

    fun validateForm(): Boolean {
        return true
    }

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            return intent
        }
    }
}
