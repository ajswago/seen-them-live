package com.swago.seenthemlive.ui.common

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.util.Utils


class ConcertListAdapter(
    private val concerts: List<ConcertItem>,
    private val artistSelectedListener: ArtistSelectedListener) : RecyclerView.Adapter<ConcertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ConcertViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ConcertViewHolder, position: Int) {
        val concert: ConcertItem = concerts[position]
        holder.artistSelectedListener = artistSelectedListener
        holder.bind(concert)
    }

    override fun getItemCount(): Int = concerts.size

    interface ArtistSelectedListener {
        fun selected(artist: String, date: String)
    }
}

class ConcertViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.concert_list_item, parent, false)) {
    var artistSelectedListener: ConcertListAdapter.ArtistSelectedListener? = null
    private var concertLayout: LinearLayout? = null
    private var concertVenue: TextView? = null
    private var concertLocation: TextView? = null
    private var concertDate: TextView? = null
    private var context = parent.context

    init {
        concertLayout = itemView.findViewById(R.id.concert_item_layout)
        concertVenue = itemView.findViewById(R.id.concert_item_venue)
        concertLocation = itemView.findViewById(R.id.concert_item_location)
        concertDate = itemView.findViewById(R.id.concert_item_date)
    }

    fun bind(concert: ConcertItem) {
        concertVenue?.text = concert.venue
        concertLocation?.text = concert.location
        concert.date.let { concertDate?.text = Utils.formatDateString(it!!) }
        concertLayout.let {
            concertLayout?.removeViews(2, concertLayout!!.childCount - 2)
        }
        concert.artists?.forEach { artist ->
            val artistView = TextView(context)
            val params = LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            params.marginStart = Utils.getPixelsFromDp(30.0f, context).toInt()
            artistView.layoutParams = params
//            artistView.setPadding(Utils.getPixelsFromDp(5.0f, context).toInt())
            val padding = Utils.getPixelsFromDp(5.0f, context).toInt()
            val paddingStart = Utils.getPixelsFromDp(30.0f, context).toInt()
            artistView.setPaddingRelative(paddingStart, padding, padding, padding)
            artistView.textSize = 20.0f
            artistView.text = artist
            val outValue = TypedValue()
            context.theme
                .resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            artistView.setBackgroundResource(outValue.resourceId)
            artistView.setOnClickListener {
                concert.date.let { date ->
                    artistSelectedListener?.selected(artist, date!!)
                }
            }
//            artistView.tag = "artist"
            concertLayout?.addView(artistView)
        }
    }
}

data class ConcertItem(
    val venue: String?,
    val location: String?,
    val date: String?,
    val artists: List<String>?
)
