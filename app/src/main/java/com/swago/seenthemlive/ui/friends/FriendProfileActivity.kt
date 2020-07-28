package com.swago.seenthemlive.ui.friends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.profile.ProfileFragment

class FriendProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_profile)
        if (savedInstanceState == null) {
            val userId: String = intent.getStringExtra(INTENT_USER_ID) ?: ""
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment.newInstance(userId))
                .commitNow()
            val importButton = findViewById<ImageButton>(R.id.import_button)
            importButton.setOnClickListener {
                val intent = ImportActivity.newIntent(this, userId)
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val INTENT_USER_ID = "userId"

        fun newIntent(context: Context, userId: String) : Intent {
            val intent = Intent(context, FriendProfileActivity::class.java)
            intent.putExtra(INTENT_USER_ID, userId)
            return intent
        }
    }
}