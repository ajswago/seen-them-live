package com.swago.seenthemlive.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.api.setlistfm.Setlist
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SetlistListAdapter(private val list: List<Setlist>, private val selectListener: OnSelectListener)
    : RecyclerView.Adapter<SetlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetlistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SetlistViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SetlistViewHolder, position: Int) {
        val setlist: Setlist = list[position]
        holder.bind(setlist)
        holder.itemView.setOnClickListener {
            val selectedSetlist = list.get(holder.adapterPosition)
            selectListener.selected(selectedSetlist)
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnSelectListener {
        fun selected(setlist: Setlist)
    }
}

class SetlistViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.setlist_list_item, parent, false)) {
    private var mArtistView: TextView? = null
    private var mVenueView: TextView? = null
    private var mTourView: TextView? = null
    private var mDateView: TextView? = null


    init {
        mArtistView = itemView.findViewById(R.id.setlist_item_artist)
        mVenueView = itemView.findViewById(R.id.setlist_item_venue)
        mTourView = itemView.findViewById(R.id.setlist_item_tour)
        mDateView = itemView.findViewById(R.id.setlist_item_date)
    }

    fun bind(setlist: Setlist) {
        mArtistView?.text = setlist.artist?.name
        var venueString = StringBuilder()
        venueString.append(setlist.venue?.name)
        setlist.venue?.city.let {
            venueString.append(" (")
            venueString.append(setlist.venue?.city?.name)
            setlist.venue?.city?.state.let {
                venueString.append(", ")
                venueString.append(setlist.venue?.city?.state)
            }
            setlist.venue?.city?.country?.name.let {
                venueString.append(", ")
                venueString.append(setlist.venue?.city?.country?.name)
            }
            venueString.append(")")
        }
        mVenueView?.text = venueString.toString()
        mTourView?.text = setlist.tour?.name
        val date = LocalDate.parse(
            setlist.eventDate,
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
        )
        mDateView?.text = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH))
    }

}