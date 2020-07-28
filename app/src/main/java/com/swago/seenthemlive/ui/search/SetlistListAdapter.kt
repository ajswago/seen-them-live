package com.swago.seenthemlive.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.util.Utils

class SetlistListAdapter(
    private val setlists: List<SetlistItem>,
    private val setlistSelectedListener: SetlistSelectedListener? = null,
    private val multiSelect: Boolean = false)
    : RecyclerView.Adapter<SetlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetlistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SetlistViewHolder(inflater, parent, multiSelect)
    }

    override fun onBindViewHolder(holder: SetlistViewHolder, position: Int) {
        val setlist: SetlistItem = setlists[position]
        holder.bind(setlist)
        holder.itemView.setOnClickListener {
            val selectedSetlist = setlists[holder.adapterPosition]
            setlistSelectedListener?.selected(selectedSetlist)
            if (multiSelect) {
                val wasSelected = selectedSetlist.selected ?: false
                selectedSetlist.selected = !wasSelected
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = setlists.size

    fun selectedSetlists(): List<SetlistItem> {
        return setlists.filter { it.selected ?: false }
    }

    interface SetlistSelectedListener {
        fun selected(setlist: SetlistItem)
    }
}

class SetlistViewHolder(inflater: LayoutInflater, parent: ViewGroup,
                        private var multiSelect: Boolean
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.setlist_list_item, parent, false)) {
    private var setlistArtist: TextView? = null
    private var setlistVenue: TextView? = null
    private var setlistTour: TextView? = null
    private var setlistDate: TextView? = null
    private var setlistSelected: CheckBox? = null

    init {
        setlistArtist = itemView.findViewById(R.id.setlist_item_artist)
        setlistVenue = itemView.findViewById(R.id.setlist_item_venue)
        setlistTour = itemView.findViewById(R.id.setlist_item_tour)
        setlistDate = itemView.findViewById(R.id.setlist_item_date)
        setlistSelected = itemView.findViewById(R.id.setlist_checkbox)
    }

    fun bind(setlist: SetlistItem) {
        setlistArtist?.text = setlist.artist
        setlistVenue?.text = setlist.venue
        setlistTour?.text = setlist.tour
        setlist.date.let { setlistDate?.text = Utils.formatDateString(it!!) }
        if (multiSelect) {
            setlistSelected?.visibility = View.VISIBLE
        } else {
            setlistSelected?.visibility = View.GONE
        }
        setlistSelected?.isChecked = setlist.selected ?: false
    }

}

data class SetlistItem(
    var id: String? = null,
    var artist: String? = null,
    var venue: String? = null,
    var tour: String? = null,
    var date: String? = null,
    var selected: Boolean? = false
)
