package com.swago.seenthemlive.ui.setlist

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.ui.common.SongItem
import com.swago.seenthemlive.ui.common.SongListAdapter
import com.swago.seenthemlive.ui.common.BaseFragment
import com.swago.seenthemlive.ui.search.SearchResultActivity
import kotlinx.android.synthetic.main.setlist_fragment.*

class SetlistFragment : BaseFragment() {

    companion object {
        fun newInstance(selectedSetlist: Setlist, showOthersAtShow: Boolean = true) = SetlistFragment().apply {
            setlist = selectedSetlist
            otherArtistsAtShow = showOthersAtShow
        }
    }

    private lateinit var viewModel: SetlistViewModel

    private var content: ConstraintLayout? = null
    private var loading: ContentLoadingProgressBar? = null

    private lateinit var setlistSaveCheckbox: CheckBox
    private lateinit var setlistArtist: TextView
    private lateinit var setlistDate: TextView
    private lateinit var setlistVenue: TextView
    private lateinit var setlistTour: TextView
    private lateinit var setlistSongListLabel: TextView
    private lateinit var setlistSongListRecyclerView: RecyclerView
    private lateinit var setlistEncoreListLabel: TextView
    private lateinit var setlistEncoreListRecyclerView: RecyclerView
    private lateinit var setlistOtherArtistAtShowLayout: LinearLayout

    private lateinit var setlist: Setlist
    private var otherArtistsAtShow: Boolean = true

    private var songItems = mutableListOf<SongItem>()
    private var encoreItems = mutableListOf<SongItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.setlist_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        content = view.findViewById(R.id.profile_content)
        loading = view.findViewById(R.id.loading)

        setlistSaveCheckbox = view.findViewById(R.id.save_setlist_checkbox)
        setlistArtist = view.findViewById(R.id.setlist_detail_artist)
        setlistDate = view.findViewById(R.id.setlist_detail_date)
        setlistVenue = view.findViewById(R.id.setlist_detail_venue)
        setlistTour = view.findViewById(R.id.setlist_detail_tour)
        setlistSongListLabel = view.findViewById(R.id.setlist_label_setlist)
        setlistSongListRecyclerView = view.findViewById(R.id.song_recycler_view)
        setlistEncoreListLabel = view.findViewById(R.id.setlist_label_encore)
        setlistEncoreListRecyclerView = view.findViewById(R.id.encore_recycler_view)
        setlistOtherArtistAtShowLayout = view.findViewById(R.id.other_artist_button_layout)

        setlistSongListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SongListAdapter(songItems)
        }
        setlistEncoreListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                SongListAdapter(encoreItems)
        }

        if (otherArtistsAtShow) {
            setlistOtherArtistAtShowLayout.visibility = View.VISIBLE
            val bgDrawable = GradientDrawable()
            bgDrawable.setColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light))
            bgDrawable.setStroke(4, ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
            setlistOtherArtistAtShowLayout.background = bgDrawable
        } else {
            setlistOtherArtistAtShowLayout.visibility = View.GONE
        }

        setlistOtherArtistAtShowLayout.setOnClickListener {
                val intent = SearchResultActivity.newIntent(
                    requireContext(),
                    userId,
                    venueId = setlist.venue?.id,
                    date = setlist.eventDate,
                    excludeArtistMbid = setlist.artist?.mbid,
                    nested = true
                )
                startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SetlistViewModel::class.java)

        viewModel.isSaved.observe(viewLifecycleOwner, Observer {
            setlistSaveCheckbox.isChecked = it
        })
        viewModel.artist.observe(viewLifecycleOwner, Observer {
            setlistArtist.text = it
        })
        viewModel.date.observe(viewLifecycleOwner, Observer {
            setlistDate.text = it
        })
        viewModel.venue.observe(viewLifecycleOwner, Observer {
            setlistVenue.text = it
        })
        viewModel.tour.observe(viewLifecycleOwner, Observer {
            setlistTour.text = it
        })
        viewModel.songList.observe(viewLifecycleOwner, Observer {
            songItems.clear()
            songItems.addAll(it)
            song_recycler_view.adapter?.notifyDataSetChanged()
        })
        viewModel.encoreList.observe(viewLifecycleOwner, Observer {
            encoreItems.clear()
            encoreItems.addAll(it)
            encore_recycler_view.adapter?.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun updateUi() {
        content?.visibility = View.GONE
        loading?.show()
        viewModel.fetchUser(userId, setlist) {
            content?.visibility = View.VISIBLE
            loading?.hide()

            setlistSaveCheckbox.setOnCheckedChangeListener { _, isChecked ->
                setlistSaveCheckbox.isEnabled = false
                loading?.show()
                viewModel.saveSetlistToUser(userId, setlist, isChecked) {
                    setlistSaveCheckbox.isEnabled = true
                    loading?.hide()
                }
            }
        }
    }

}