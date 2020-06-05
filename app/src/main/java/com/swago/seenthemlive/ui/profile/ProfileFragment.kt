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
import com.google.firebase.firestore.FirebaseFirestore
import com.swago.seenthemlive.LoginActivity
import com.swago.seenthemlive.R

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var userId: String? = null

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
        content = root.findViewById(R.id.profile_content)
        loading = root.findViewById(R.id.loading)

        userId = activity?.intent?.getStringExtra("user")

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
            profileConcertCount.text = "Number of Concerts Attended: ${it}"
        })
        profileViewModel.userBandCount.observe(this, Observer {
            profileBandsCount.text = "Number of Bands Seen: ${it}"
        })
        profileViewModel.userVenueCount.observe(this, Observer {
            profileVenuesCount.text = "Number of Venues Attended: ${it}"
        })

        updateUi()

        return root
    }

    fun updateUi() {
        content?.visibility = View.GONE
        loading?.show()
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId!!)
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
                val topArtists = user?.setlists?.groupBy { it.artist?.name }?.toList()
                    ?.sortedByDescending { (_, value) -> value.size }?.take(10)?.toMap()
                Log.d("Profile", "Top Five Artists: ${topArtists}")
                topArtists?.forEach { artistMap -> Log.d("Profile", "Artist: ${artistMap.key}, Count: ${artistMap.value.size}") }
                profileViewModel.userDisplayName.postValue(user?.displayName)
                profileViewModel.userUsername.postValue(user?.username)
                profileViewModel.userEmail.postValue(user?.email)
                profileViewModel.userConcertCount.postValue(concertCount)
                profileViewModel.userBandCount.postValue(bandCount)
                profileViewModel.userVenueCount.postValue(venueCount)

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