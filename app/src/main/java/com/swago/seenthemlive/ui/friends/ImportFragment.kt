package com.swago.seenthemlive.ui.friends

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.BaseFragment
import com.swago.seenthemlive.ui.search.SetlistItem
import com.swago.seenthemlive.ui.search.SetlistListAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ImportFragment : BaseFragment() {

    companion object {
        fun newInstance(profileId: String) = ImportFragment().apply {
            this.profileId = profileId
        }
    }

    private lateinit var importViewModel: ImportViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var profileId: String = ""
    private var importSetlists = mutableListOf<SetlistItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        importViewModel =
            ViewModelProvider(this).get(ImportViewModel::class.java)

        val root = inflater.inflate(R.layout.import_fragment, container, false)
        val importRecyclerView: RecyclerView = root.findViewById(R.id.import_recycler_view)
        val importButton: Button = root.findViewById(R.id.import_button)
        content = root.findViewById(R.id.profile_content)
        loading = root.findViewById(R.id.loading)

        importRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SetlistListAdapter(importSetlists, multiSelect = true)
        }

        importButton.setOnClickListener {
            val adapter: SetlistListAdapter = importRecyclerView.adapter as SetlistListAdapter
            Log.d("IMPORT", "Importing Setlist Artists: ${adapter.selectedSetlists().map { it.artist }}")
        }

        importViewModel.importSetlists.observe(viewLifecycleOwner, Observer {setlists ->
            importSetlists.clear()
            importSetlists.addAll(setlists.sortedByDescending { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)) })
            importRecyclerView.adapter?.notifyDataSetChanged()
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
        importViewModel.fetchSetlists(userId, profileId) {
            content?.visibility = View.VISIBLE
            loading?.hide()
        }
    }
}