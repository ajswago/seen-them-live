package com.swago.seenthemlive.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.BaseFragment
import com.swago.seenthemlive.ui.common.CountedItem
import com.swago.seenthemlive.ui.common.CountedListAdapter
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A [Fragment] subclass for display of user profile info
 */
class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance(profileId: String) = ProfileFragment().apply {
            this.profileId = profileId
        }
    }

    private lateinit var profileViewModel: ProfileViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var profileId: String = userId
    private var topArtists = mutableListOf<CountedItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val profileDisplayName: TextView = root.findViewById(R.id.profile_display_name)
        val profileUsername: TextView = root.findViewById(R.id.profile_username)
        val profileEmail: TextView = root.findViewById(R.id.profile_email)
        val profileConcertCount: TextView = root.findViewById(R.id.profile_num_shows_label)
        val profileBandsCount: TextView = root.findViewById(R.id.profile_num_bands_label)
        val profileVenuesCount: TextView = root.findViewById(R.id.profile_num_venues_label)
        val topArtistRecyclerView: RecyclerView = root.findViewById(R.id.top_artist_recycler_view)
        content = root.findViewById(R.id.profile_content)
        loading = root.findViewById(R.id.loading)

        topArtistRecyclerView.apply {
            layoutManager = nonScrollingLayoutManager()
            adapter = CountedListAdapter(topArtists)
        }

        profileViewModel.userDisplayName.observe(viewLifecycleOwner, Observer {
            profileDisplayName.text = it
        })
        profileViewModel.userUsername.observe(viewLifecycleOwner, Observer {
            profileUsername.text = it
        })
        profileViewModel.userEmail.observe(viewLifecycleOwner, Observer {
            profileEmail.text = it
        })
        profileViewModel.userConcertCount.observe(viewLifecycleOwner, Observer {
            profileConcertCount.text = getString(R.string.profile_concerts_count_format, it)
        })
        profileViewModel.userBandCount.observe(viewLifecycleOwner, Observer {
            profileBandsCount.text = getString(R.string.profile_bands_count_format, it)
        })
        profileViewModel.userVenueCount.observe(viewLifecycleOwner, Observer {
            profileVenuesCount.text = getString(R.string.profile_venues_count_format, it)
        })
        profileViewModel.userTopArtists.observe(viewLifecycleOwner, Observer {
            topArtists.clear()
            topArtists.addAll(it)
            top_artist_recycler_view.adapter?.notifyDataSetChanged()
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
        profileViewModel.fetchUser(profileId) {
            content?.visibility = View.VISIBLE
            loading?.hide()
        }
    }
}