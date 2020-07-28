package com.swago.seenthemlive.ui.friends

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swago.seenthemlive.R

class ImportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_activity)
        if (savedInstanceState == null) {
            val profileId: String = intent.getStringExtra(INTENT_PROFILE_ID) ?: ""
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ImportFragment.newInstance(profileId))
                .commitNow()
        }
    }

    companion object {
        private const val INTENT_PROFILE_ID = "profileId"

        fun newIntent(context: Context, profileId: String) : Intent {
            val intent = Intent(context, ImportActivity::class.java)
            intent.putExtra(INTENT_PROFILE_ID, profileId)
            return intent
        }
    }
}