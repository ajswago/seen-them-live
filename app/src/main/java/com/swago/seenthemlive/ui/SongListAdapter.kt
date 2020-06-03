package com.swago.seenthemlive.ui

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
        Log.d("Song", "SongItem: ${song}")
        holder.bind(song)
    }

    override fun getItemCount(): Int = songs.size
}

class SongViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.song_list_item, parent, false)) {
    private var mSongIndex: TextView? = null
    private var mSongName: TextView? = null

    init {
        mSongIndex = itemView.findViewById(R.id.song_index)
        mSongName = itemView.findViewById(R.id.song_name)
    }

    fun bind(song: SongItem) {
        mSongIndex?.text = "${song.index}."
        mSongName?.text = song.name
    }
}

data class SongItem(
    val index: Int?,
    val name: String?
)
