package com.swago.seenthemlive.ui.artist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ArtistFragment : BaseFragment() {
    
    companion object {
        fun newInstance(profileId: String) = ArtistFragment().apply { 
            this.profileId = profileId
        }
    }

    private lateinit var artistViewModel: ArtistViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var profileId: String = userId
    private var artists = mutableListOf<ArtistItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artistViewModel =
            ViewModelProvider(this).get(ArtistViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_artist, container, false)
        val artistRecyclerView: RecyclerView = root.findViewById(R.id.artist_recycler_view)
        content = root.findViewById(R.id.artist_content)
        loading = root.findViewById(R.id.loading)

        artistRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ArtistListAdapter(artists, object : ArtistListAdapter.ArtistSelectedListener {
                override fun selected(artistId: String) {
                    Log.d("ArtistFragment", "Artist Selected ${artistId}")
                }
            })
        }

        artistViewModel.artists.observe(viewLifecycleOwner, Observer {
            artists.clear()
            artists.addAll(it)
            artistRecyclerView.adapter?.notifyDataSetChanged()
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
        artistViewModel.fetchUser(profileId) {
            content?.visibility = View.VISIBLE
            loading?.hide()
        }
    }
}
