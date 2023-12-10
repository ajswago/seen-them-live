package com.swago.seenthemlive.ui.artist

import android.os.Bundle
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

class ArtistDetailFragment : BaseFragment() {

    companion object {
        fun newInstance(artistId: String) = ArtistDetailFragment().apply {
            this.artistId = artistId
        }
    }

    private lateinit var artistDetailViewModel: ArtistDetailViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var profileId: String = userId
    private var artistId: String = ""
    private var concerts = mutableListOf<ConcertItem>()
    private var topSongs = mutableListOf<CountedItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artistDetailViewModel =
            ViewModelProvider(this).get(ArtistDetailViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_artist_detail, container, false)
        val artistName: TextView = root.findViewById(R.id.artist_detail_name)
        val artistShowCount: TextView = root.findViewById(R.id.artist_detail_count)
        val artistSetlistsRecyclerView: RecyclerView = root.findViewById(R.id.shows_recycler_view)
        val artistSongsRecyclerView: RecyclerView = root.findViewById(R.id.songs_recycler_view)
        content = root.findViewById(R.id.artist_detail_content)
        loading = root.findViewById(R.id.loading)

        artistSetlistsRecyclerView.apply {
            layoutManager = nonScrollingLayoutManager()
            adapter = ConcertListAdapter(concerts, individualArtistViews = false)
        }
        artistSongsRecyclerView.apply {
            layoutManager = nonScrollingLayoutManager()
            adapter = CountedListAdapter(topSongs)
        }

        artistDetailViewModel.artistName.observe(viewLifecycleOwner, Observer {
            artistName.text = it
        })
        artistDetailViewModel.artistSetlistCount.observe(viewLifecycleOwner, Observer {
            artistShowCount.text = context?.getString(R.string.artist_item_count_format, it)
        })
        artistDetailViewModel.artistConcerts.observe(viewLifecycleOwner, Observer {
            concerts.clear()
            concerts.addAll(it)
            artistSetlistsRecyclerView.adapter?.notifyDataSetChanged()
        })
        artistDetailViewModel.artistTopSongs.observe(viewLifecycleOwner, Observer {
            topSongs.clear()
            topSongs.addAll(it)
            artistSongsRecyclerView.adapter?.notifyDataSetChanged()
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
        artistDetailViewModel.fetchUserArtist(profileId, artistId) {
            content?.visibility = View.VISIBLE
            loading?.hide()
        }
    }
}
