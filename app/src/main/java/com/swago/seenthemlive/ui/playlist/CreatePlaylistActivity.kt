package com.swago.seenthemlive.ui.playlist

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import com.swago.seenthemlive.BuildConfig
import com.swago.seenthemlive.R
import com.swago.seenthemlive.api.spotify.ProfileRepository
import com.swago.seenthemlive.api.spotify.SpotifyApiBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class CreatePlaylistActivity : AppCompatActivity() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val SCOPES =
        "user-read-email,user-read-private,playlist-modify-private,playlist-modify-public"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_playlist_activity)
        if (savedInstanceState == null) {
            checkSpotifyAuth()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(
            requestCode, resultCode,
            intent
        )

        // Check if result comes from the our Spotify login activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(
                resultCode, intent
            )
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    // we'll store the auth token in shared preferences
                    val editor = getSharedPreferences(
                        getString(
                            R.string.spotify_shared_pref_key
                        ),
                        MODE_PRIVATE
                    )
                        .edit()
                    editor?.putString(
                        "token",
                        response.accessToken
                    )
                    editor?.apply()
                    checkSpotifyAuth()
                }

                AuthorizationResponse.Type.ERROR -> Log.d("LoginActivity", response.error)
                else -> Log.d("LoginActivity", response.toString())
            }
        }
    }

    fun checkSpotifyAuth() {
        val editor = this.getSharedPreferences(
            getString(R.string.spotify_shared_pref_key),
            MODE_PRIVATE)
        val token = editor.getString("token", "")

        token?.let {
            val repository : ProfileRepository = ProfileRepository(SpotifyApiBuilder.profile(it))
            scope.launch {
                val profile = repository.getProfile()
                if (profile == null) {
                    GlobalScope.launch(Dispatchers.Main) {
                        authenticateWithSpotify()
                    }
                } else {
                    val profileId: String = intent.getStringExtra(INTENT_PROFILE_ID) ?: ""
                    val spotifyDisplayName = profile.display_name
                    GlobalScope.launch(Dispatchers.Main) {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.container,
                                CreatePlaylistFragment.newInstance(profileId, spotifyDisplayName)
                            )
                            .commitNow()
                    }
                }
            }
        } ?: authenticateWithSpotify()

    }

    private fun authenticateWithSpotify() {

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("In order to create a playlist you must authenticate with Spotify. Would you like to proceed to Spotify authentication?")
        dialogBuilder.setTitle("Authenticate With Spotify")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            // Code to authenticate our Spotify app
            val builder = AuthorizationRequest.Builder(
                BuildConfig.SPOTIFY_CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                getString(R.string.spotify_auth_redirect_uri)
            )

            builder.setScopes(arrayOf(SCOPES))
            val request = builder.build()

            // Opens the Login Activity Web page for spotify for auth
            AuthorizationClient.openLoginActivity(
                this, REQUEST_CODE, request
            )
        }
        dialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
            finish()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        private const val INTENT_PROFILE_ID = "profileId"

        fun newIntent(context: Context, profileId: String) : Intent {
            val intent = Intent(context, CreatePlaylistActivity::class.java)
            intent.putExtra(INTENT_PROFILE_ID, profileId)
            return intent
        }
    }
}