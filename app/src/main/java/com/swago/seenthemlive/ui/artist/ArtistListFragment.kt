package com.swago.seenthemlive.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.ArtistItem
import com.swago.seenthemlive.ui.common.ArtistListAdapter
import com.swago.seenthemlive.ui.common.BaseFragment

class ArtistListFragment : BaseFragment() {
    
    companion object {
        fun newInstance(profileId: String) = ArtistListFragment().apply {
            this.profileId = profileId
        }
    }

    private lateinit var artistListViewModel: ArtistListViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var profileId: String = userId
    private var artists = mutableListOf<ArtistItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artistListViewModel =
            ViewModelProvider(this).get(ArtistListViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_artist_list, container, false)
        val artistRecyclerView: RecyclerView = root.findViewById(R.id.artist_recycler_view)
        content = root.findViewById(R.id.artist_content)
        loading = root.findViewById(R.id.loading)

        artistRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ArtistListAdapter(artists, object : ArtistListAdapter.ArtistSelectedListener {
                override fun selected(artistId: String) {
                    val intent = ArtistDetailActivity.newIntent(context, artistId)
                    startActivity(intent)
                }
            })
        }

        artistListViewModel.artists.observe(viewLifecycleOwner, Observer {
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
        artistListViewModel.fetchUser(profileId) {
            content?.visibility = View.VISIBLE
            loading?.hide()
        }
    }
}
