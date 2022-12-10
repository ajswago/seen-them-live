package com.swago.seenthemlive.ui.common

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R

class SongListAdapter(private val songs: List<SongItem>) : RecyclerView.Adapter<SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SongViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song: SongItem = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int = songs.size
}

class SongViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.song_list_item, parent, false)) {
    private var mSongIndex: TextView? = null
    private var mSongName: TextView? = null
    private var context = parent.context

    init {
        mSongIndex = itemView.findViewById(R.id.song_index)
        mSongName = itemView.findViewById(R.id.song_name)
    }

    fun bind(song: SongItem) {
        if (song.index != null) {
            mSongIndex?.text = context
                .getString(R.string.counted_item_list_index_format, song.index)
            mSongName?.setTextColor(Color.DKGRAY)
        } else {
            mSongIndex?.text = ""
            mSongName?.setTextColor(Color.LTGRAY)
        }
        if (song.coverArtist != null) {
            mSongName?.text = "${song.name} (${song.coverArtist} cover)"
        } else {
            mSongName?.text = song.name
        }
    }
}

data class SongItem(
    val index: Int?,
    val name: String?,
    val coverArtist: String?
)
