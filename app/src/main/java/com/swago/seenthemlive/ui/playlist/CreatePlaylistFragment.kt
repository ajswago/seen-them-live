package com.swago.seenthemlive.ui.playlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.BaseFragment
import com.swago.seenthemlive.ui.search.SetlistItem
import com.swago.seenthemlive.ui.search.SetlistListAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreatePlaylistFragment : BaseFragment() {

    companion object {
        fun newInstance(profileId: String, spotifyDisplayName: String) = CreatePlaylistFragment().apply {
            this.profileId = profileId
            this.spotifyDisplayName = spotifyDisplayName
        }
    }

    private lateinit var createPlaylistViewModel: CreatePlaylistViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null
    private lateinit var signedInUsername: TextView
    private lateinit var playlistNameField: EditText

    private var profileId: String = userId
    private var spotifyDisplayName: String = ""
    private var playlistSetlists = mutableListOf<SetlistItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createPlaylistViewModel =
            ViewModelProvider(this).get(CreatePlaylistViewModel::class.java)

        val root = inflater.inflate(R.layout.create_playlist_fragment, container, false)
        val createPlaylistRecyclerView: RecyclerView = root.findViewById(R.id.create_playlist_recycler_view)
        val createPlaylistButton: FloatingActionButton = root.findViewById(R.id.create_playlist_button)
        content = root.findViewById(R.id.create_playlist_content)
        loading = root.findViewById(R.id.loading)
        signedInUsername = root.findViewById(R.id.signed_in_username)
        playlistNameField = root.findViewById(R.id.playlist_name_field)

        signedInUsername.text = spotifyDisplayName

        createPlaylistRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SetlistListAdapter(playlistSetlists, multiSelect = true)
        }
        createPlaylistRecyclerView.addItemDecoration(MaterialDividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
            dividerInsetEnd = 56
            dividerInsetStart = 56
            dividerThickness = 4
            dividerColor = resources.getColor(R.color.colorAccent, null)
        })

        createPlaylistButton.setOnClickListener {
            val editor = context?.getSharedPreferences(
                getString(R.string.spotify_shared_pref_key),
                AppCompatActivity.MODE_PRIVATE
            )
            val token = editor?.getString("token", "")

            token?.let {
                loading?.show()
                val adapter: SetlistListAdapter =
                    createPlaylistRecyclerView.adapter as SetlistListAdapter
                createPlaylistViewModel.createPlaylistFromSelections(
                    token,
                    userId,
                    adapter.selectedSetlists().map { it.id ?: "" },
                    playlistNameField.text.toString()
                ) {
                    loading?.hide()

                    val dialogBuilder = AlertDialog.Builder(context)
                    dialogBuilder.setMessage("Your playlist was successfully created in your Spotify account!")
                    dialogBuilder.setTitle("Success")
                    dialogBuilder.setCancelable(true)
                    dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                        activity?.finish()
                    }
                    val alertDialog = dialogBuilder.create()
                    alertDialog.show()
                }
            } ?: (activity as? CreatePlaylistActivity)?.checkSpotifyAuth()
        }

        createPlaylistViewModel.createPlaylistSetlists.observe(viewLifecycleOwner, Observer {setlists ->
            playlistSetlists.clear()
            playlistSetlists.addAll(setlists.sortedByDescending { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)) })
            createPlaylistRecyclerView.adapter?.notifyDataSetChanged()
        })

        return root
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun updateUi() {
        content?.visibility = View.GONE
        loading?.show()
        createPlaylistViewModel.fetchSetlists(userId) {
            content?.visibility = View.VISIBLE
            loading?.hide()
        }
    }
}