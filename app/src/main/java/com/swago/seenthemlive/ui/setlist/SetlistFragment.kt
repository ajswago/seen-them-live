package com.swago.seenthemlive.ui.setlist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
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
        fun newInstance(selectedSetlist: Setlist, showOthersAtShow: Boolean = true, originSetlist: Setlist? = null) = SetlistFragment().apply {
            setlist = selectedSetlist
            otherArtistsAtShow = showOthersAtShow
            setlistToUpdate = originSetlist
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
    private lateinit var setlistEncore2ListLabel: TextView
    private lateinit var setlistEncore2ListRecyclerView: RecyclerView
    private lateinit var setlistEncore3ListLabel: TextView
    private lateinit var setlistEncore3ListRecyclerView: RecyclerView
    private lateinit var setlistOtherArtistAtShowLayout: LinearLayout
    private lateinit var setlistOtherArtistAtShowButton: Button
    private lateinit var setlistEditButton: ImageButton

    private lateinit var setlist: Setlist
    private var otherArtistsAtShow: Boolean = true
    private var setlistToUpdate: Setlist? = null

    private var songItems = mutableListOf<SongItem>()
    private var encoreItems = mutableListOf<SongItem>()
    private var encore2Items = mutableListOf<SongItem>()
    private var encore3Items = mutableListOf<SongItem>()

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
        setlistEncore2ListLabel = view.findViewById(R.id.setlist_label_encore2)
        setlistEncore2ListRecyclerView = view.findViewById(R.id.encore2_recycler_view)
        setlistEncore3ListLabel = view.findViewById(R.id.setlist_label_encore3)
        setlistEncore3ListRecyclerView = view.findViewById(R.id.encore3_recycler_view)
        setlistOtherArtistAtShowLayout = view.findViewById(R.id.setlist_additional_artists_layout)
        setlistOtherArtistAtShowButton = view.findViewById(R.id.setlist_additional_artists_button)
        setlistEditButton = view.findViewById(R.id.setlist_edit_button)

        setlistSongListRecyclerView.apply {
            layoutManager = nonScrollingLayoutManager()
            adapter = SongListAdapter(songItems)
        }
        setlistEncoreListRecyclerView.apply {
            layoutManager = nonScrollingLayoutManager()
            adapter =
                SongListAdapter(encoreItems)
        }
        setlistEncore2ListRecyclerView.apply {
            layoutManager = nonScrollingLayoutManager()
            adapter =
                SongListAdapter(encore2Items)
        }
        setlistEncore3ListRecyclerView.apply {
            layoutManager = nonScrollingLayoutManager()
            adapter =
                SongListAdapter(encore3Items)
        }

        if (otherArtistsAtShow) {
            setlistOtherArtistAtShowLayout.visibility = View.VISIBLE
        } else {
            setlistOtherArtistAtShowLayout.visibility = View.GONE
        }

        setlistOtherArtistAtShowButton.setOnClickListener {
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

        if(setlistToUpdate != null) {
            setlistEditButton.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_save))
        } else {
            setlistEditButton.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_edit))
        }
        setlistEditButton.setOnClickListener {
            if(setlistToUpdate != null) {
                saveOverSetlist()
            } else {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("If your show setlist is missing or incomplete you can try to find setlists from other shows on the same tour. Do you want to proceed?")
                builder.setTitle("Search For Setlists")
                builder.setCancelable(true)
                builder.setPositiveButton("Yes") { dialog, _ ->
                    setlistSearch()
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }
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
            if (encoreItems.isNotEmpty())
                setlistEncoreListLabel.visibility = View.VISIBLE
            else setlistEncoreListLabel.visibility = View.GONE
        })
        viewModel.encore2List.observe(viewLifecycleOwner, Observer {
            encore2Items.clear()
            encore2Items.addAll(it)
            encore2_recycler_view.adapter?.notifyDataSetChanged()
            if (encore2Items.isNotEmpty())
                setlistEncore2ListLabel.visibility = View.VISIBLE
            else setlistEncore2ListLabel.visibility = View.GONE
        })
        viewModel.encore3List.observe(viewLifecycleOwner, Observer {
            encore3Items.clear()
            encore3Items.addAll(it)
            encore3_recycler_view.adapter?.notifyDataSetChanged()
            if (encore3Items.isNotEmpty())
                setlistEncore3ListLabel.visibility = View.VISIBLE
            else setlistEncore3ListLabel.visibility = View.GONE
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

    private fun setlistSearch() {
        val intent = SearchResultActivity.newIntent(
            requireContext(),
            userId,
            tourName = setlist.tour?.name,
            artistMbid = setlist.artist?.mbid,
            year = setlist.eventDate?.takeLast(4),
            countryCode = setlist.venue?.city?.country?.code,
            nested = true,
            originSetlist = setlist
        )
        startActivityForResult(intent, 0)
    }

    private fun saveOverSetlist() {
        val updatedSetlist = setlistToUpdate?.copy(sets = setlist.sets)
        updatedSetlist?.let {
            loading?.show()
            viewModel.overwriteSetlistToUser(userId, updatedSetlist) {
                loading?.hide()
                val intent = Intent()
                intent.putExtra(SetlistActivity.INTENT_UPDATED_SETLIST, updatedSetlist)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
    }

}