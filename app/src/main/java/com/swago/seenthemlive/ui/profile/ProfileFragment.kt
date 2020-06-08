package com.swago.seenthemlive.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.swago.seenthemlive.BaseActivity
import com.swago.seenthemlive.LoginActivity
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.CountedItem
import com.swago.seenthemlive.ui.common.CountedListAdapter
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A [Fragment] subclass for display of user profile info
 */
class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var topArtists = mutableListOf<CountedItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)

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
            layoutManager = LinearLayoutManager(context)
            adapter = CountedListAdapter(topArtists)
        }

        profileViewModel.userDisplayName.observe(this, Observer {
            profileDisplayName.text = it
        })
        profileViewModel.userUsername.observe(this, Observer {
            profileUsername.text = it
        })
        profileViewModel.userEmail.observe(this, Observer {
            profileEmail.text = it
        })
        profileViewModel.userConcertCount.observe(this, Observer {
            profileConcertCount.text = getString(R.string.profile_concerts_count_format, it)
        })
        profileViewModel.userBandCount.observe(this, Observer {
            profileBandsCount.text = getString(R.string.profile_bands_count_format, it)
        })
        profileViewModel.userVenueCount.observe(this, Observer {
            profileVenuesCount.text = getString(R.string.profile_venues_count_format, it)
        })
        profileViewModel.userTopArtists.observe(this, Observer {
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

    fun updateUi() {
        content?.visibility = View.GONE
        loading?.show()
        val db = FirebaseFirestore.getInstance()
        val userId = (activity as BaseActivity).uid
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { documentReference ->
                val user: LoginActivity.User? = documentReference.toObject(LoginActivity.User::class.java)
                val concertCount = user?.setlists?.groupBy {
                    it.eventDate
                }?.size
                val bandCount = user?.setlists?.groupBy {
                    it.artist?.mbid
                }?.size
                val venueCount = user?.setlists?.groupBy {
                    it.venue?.id
                }?.size
                val artistsByCount = user?.setlists
                    ?.groupBy { it.artist?.name }
                    ?.map { CountedItem(it.key, it.value.size) }

                profileViewModel.userDisplayName.postValue(user?.displayName)
                profileViewModel.userUsername.postValue(user?.username)
                profileViewModel.userEmail.postValue(user?.email)
                profileViewModel.userConcertCount.postValue(concertCount)
                profileViewModel.userBandCount.postValue(bandCount)
                profileViewModel.userVenueCount.postValue(venueCount)
                profileViewModel.setTopArtists(artistsByCount ?: ArrayList())

                content?.visibility = View.VISIBLE
                loading?.hide()
            }
            .addOnFailureListener { e ->
                Log.w("CLOUDFIRESTORE", "Error adding document", e)
                content?.visibility = View.VISIBLE
                loading?.hide()
            }
    }
}