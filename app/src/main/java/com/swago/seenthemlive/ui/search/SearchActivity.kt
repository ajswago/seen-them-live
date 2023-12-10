package com.swago.seenthemlive.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.swago.seenthemlive.LoginActivity
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ViewConcertsActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val userId = intent.getStringExtra(INTENT_USER)

        val artistField: EditText = findViewById(R.id.artist_field)
        val venueField: EditText = findViewById(R.id.venue_field)
        val searchButton: Button = findViewById(R.id.search_button)
        searchButton.setOnClickListener { view ->
            if (validateForm()) {
                var stateCode = state_spinner.selectedItem as String?
                if (stateCode.equals(resources.getString(R.string.state_empty_value))) {
                    stateCode = null
                }
                val intent = SearchResultActivity.newIntent(
                    this,
                    userId = userId!!,
                    artistMbid = null, artistName = artistField.text.toString(),
                    venueName = venueField.text.toString(),
                    stateCode = stateCode)
                startActivity(intent)
            }
        }

        val states = mutableListOf<String>()
        states.add(resources.getString(R.string.state_empty_value))
        states.addAll(resources.getStringArray(R.array.States))
        state_spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            states)
        state_spinner.setSelection(0)
    }

    fun validateForm(): Boolean {
        return true
    }

    companion object {

        private val INTENT_USER = "user"

        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(INTENT_USER, userId)
            return intent
        }
    }
}
