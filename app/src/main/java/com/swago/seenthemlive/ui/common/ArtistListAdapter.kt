package com.swago.seenthemlive.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.util.Utils

class ArtistListAdapter(
    private val artists: List<ArtistItem>,
    private val artistSelectedListener: ArtistSelectedListener) : RecyclerView.Adapter<ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtistViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist: ArtistItem = artists[position]
        holder.artistSelectedListener = artistSelectedListener
        holder.bind(artist)
    }

    override fun getItemCount(): Int = artists.size

    interface ArtistSelectedListener {
        fun selected(artistId: String)
    }
}

class ArtistViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.artist_list_item, parent, false)) {
    var artistSelectedListener: ArtistListAdapter.ArtistSelectedListener? = null
    private var artistLayout: LinearLayout? = null
    private var artistName: TextView? = null
    private var artistCount: TextView? = null
    private var artistLatestDate: TextView? = null
    private var context = parent.context

    init {
        artistLayout = itemView.findViewById(R.id.artist_item_layout)
        artistName = itemView.findViewById(R.id.artist_item_name)
        artistCount = itemView.findViewById(R.id.artist_item_count)
        artistLatestDate = itemView.findViewById(R.id.artist_item_latest_concert)
    }

    fun bind(artist: ArtistItem) {
        artistName?.text = artist.artistName
        artistCount?.text = context.getString(R.string.artist_item_count_format, artist.totalCount)
        artist.latestDate.let {
            artistLatestDate?.text = context.getString(
                R.string.artist_item_latest_date_format,
                Utils.formatDateString(it!!)
            )
        }
        artistLayout?.setOnClickListener {
            artist.artistId.let {
                artistSelectedListener?.selected(it!!)
            }
        }
    }
}

data class ArtistItem(
    val artistId: String?,
    val artistName: String?,
    val firstDate: String?,
    val latestDate: String?,
    val firstVenue: String?,
    val latestVenue: String?,
    val totalCount: Int?
)