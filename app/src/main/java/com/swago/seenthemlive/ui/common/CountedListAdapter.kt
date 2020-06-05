package com.swago.seenthemlive.ui.common

import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R

class CountedListAdapter(private val countedList: List<CountedItem>) : RecyclerView.Adapter<CountedItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountedItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CountedItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CountedItemViewHolder, position: Int) {
        val countedItem: CountedItem = countedList[position]
        holder.bind(countedItem)
    }

    override fun getItemCount(): Int = countedList.size

}

class CountedItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.counted_list_item, parent, false)) {
    private var mCountedItemIndex: TextView? = null
    private var mCountedItemName: TextView? = null
    private var mCountedItemCount: TextView? = null
    
    init {
        mCountedItemIndex = itemView.findViewById(R.id.counted_item_index)
        mCountedItemName = itemView.findViewById(R.id.counted_item_name)
        mCountedItemCount = itemView.findViewById(R.id.counted_item_count)
    }
    
    fun bind(countedItem: CountedItem) {
        mCountedItemIndex?.text = "${adapterPosition+1}."
        mCountedItemName?.text = countedItem.name
        mCountedItemCount?.text = "(${countedItem.count})"
    }
}

data class CountedItem(
    val name: String? = null,
    val count: Int? = null
)