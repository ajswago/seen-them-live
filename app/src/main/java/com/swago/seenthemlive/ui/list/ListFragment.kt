package com.swago.seenthemlive.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import com.swago.seenthemlive.LoginActivity
import com.swago.seenthemlive.R
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.ui.common.ConcertItem
import com.swago.seenthemlive.ui.common.ConcertListAdapter
import com.swago.seenthemlive.ui.setlist.SetlistActivity
import com.swago.seenthemlive.util.Utils
import kotlinx.android.synthetic.main.setlist_fragment.setlist
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel

    private var loading: ContentLoadingProgressBar? = null

    private val setlists = mutableListOf<Setlist>()
    private val concerts = mutableListOf<ConcertItem>()
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel =
            ViewModelProvider(this).get(ListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        val setlistRecyclerView: RecyclerView = root.findViewById(R.id.setlist_recycler_view)
        val noContentView: TextView = root.findViewById(R.id.setlist_list_no_content_label)
        loading = root.findViewById(R.id.loading)

        userId = activity?.intent?.getStringExtra("user")
        listViewModel.setlists.observe(viewLifecycleOwner, Observer { updatedSetlists ->
            if (updatedSetlists.isEmpty()) {
                noContentView.visibility = View.VISIBLE
                setlistRecyclerView.visibility = View.GONE
            } else {
                setlists.clear()
                setlists.addAll(updatedSetlists)
                concerts.clear()
                val concertItems = updatedSetlists.groupBy { it.eventDate }
                    .map { group ->
                        var location: String
                        val venue = group.value.first().venue
                        venue.let { location = Utils.formatLocationString(venue!!) }
                        ConcertItem(group.value.first().venue?.name, location, group.key, group.value.map { it.artist?.name ?: "" })
                    }
                concerts.addAll(concertItems.sortedByDescending { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)) })
                setlistRecyclerView.adapter?.notifyDataSetChanged()
                noContentView.visibility = View.GONE
                setlistRecyclerView.visibility = View.VISIBLE
            }
        })
        setlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ConcertListAdapter(concerts, object : ConcertListAdapter.ArtistSelectedListener {
                override fun selected(artist: String, date: String) {
                    val setlistMatch = setlists.find { setlist -> setlist.artist?.name.equals(artist) && setlist.eventDate.equals(date) }
                    setlistMatch.let { setlist ->
                        val intent = SetlistActivity.newIntent(context, setlist!!)
                        startActivity(intent)
                    }
                }
            })
        }
        setlistRecyclerView.addItemDecoration(MaterialDividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
            dividerInsetEnd = 56
            dividerInsetStart = 56
            dividerThickness = 4
            dividerColor = resources.getColor(R.color.colorAccent, null)
        })
        return root
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun updateUi() {
        loading?.show()
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId!!)
            .get()
            .addOnSuccessListener { documentReference ->
                val user: LoginActivity.User? = documentReference.toObject(LoginActivity.User::class.java)
                val userSetlists = user?.setlists ?: ArrayList()
                listViewModel.setlists.postValue(userSetlists)
                loading?.hide()
            }
            .addOnFailureListener { e ->
                Log.w("CLOUDFIRESTORE", "Error getting document", e)
                loading?.hide()
            }
    }
}