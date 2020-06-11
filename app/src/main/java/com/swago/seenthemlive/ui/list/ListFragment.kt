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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.swago.seenthemlive.LoginActivity
import com.swago.seenthemlive.R
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.ui.setlist.SetlistDetailActivity
import com.swago.seenthemlive.ui.search.SetlistListAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel

    private var loading: ContentLoadingProgressBar? = null

    private val setlists = mutableListOf<Setlist>()
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
        listViewModel.setlists.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                noContentView.visibility = View.VISIBLE
                setlistRecyclerView.visibility = View.GONE
            } else {
                setlists.clear()
                setlists.addAll(it.sortedByDescending {
                    LocalDate.parse(
                        it.eventDate,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                    )
                })
                setlistRecyclerView.adapter?.notifyDataSetChanged()
                noContentView.visibility = View.GONE
                setlistRecyclerView.visibility = View.VISIBLE
            }
        })
        Log.d("RECYCLER", "${setlist_recycler_view}")
        setlistRecyclerView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = SetlistListAdapter(setlists, object : SetlistListAdapter.OnSelectListener {
                override fun selected(setlist: Setlist) {
                    Log.d("SEARCH RESULT", "SELECTED SETLIST: ${setlist}")
                    val intent = SetlistDetailActivity.newIntent(context, userId!!, setlist)
                    startActivity(intent)
                }
            })
        }
        val db = FirebaseFirestore.getInstance()
//        updateUi()
        return root
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    fun updateUi() {
        loading?.show()
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId!!)
            .get()
            .addOnSuccessListener { documentReference ->
                val user: LoginActivity.User? = documentReference.toObject(LoginActivity.User::class.java)
//                if (user?.setlists == null) {
//                    user?.setlists = ArrayList()
//                }
                val userSetlists = user?.setlists ?: ArrayList()
                Log.d("CLOUDFIRESTORE", "DocumentSnapshot retrieved with ID: ${user}")
                listViewModel.setlists.postValue(userSetlists!!)
                loading?.hide()
            }
            .addOnFailureListener { e ->
                Log.w("CLOUDFIRESTORE", "Error adding document", e)
                loading?.hide()
            }
    }
}