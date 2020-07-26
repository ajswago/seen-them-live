package com.swago.seenthemlive.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R

class UserListAdapter(
    private val userList: List<UserItem>,
    private val userSelectedListener: UserSelectedListener) : RecyclerView.Adapter<UserItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val userItem: UserItem = userList[position]
        holder.bind(userItem)
        holder.itemView.setOnClickListener {
            val selectedUser = userList[holder.adapterPosition]
            selectedUser.id.let { userSelectedListener.selected(it!!) }
        }
    }

    override fun getItemCount(): Int = userList.size

    interface UserSelectedListener {
        fun selected(id: String)
    }
}

class UserItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.user_list_item, parent, false)) {
    private var userItemDisplayName: TextView? = null
    private var userItemEmail: TextView? = null

    init {
        userItemDisplayName = itemView.findViewById(R.id.user_item_display_name)
        userItemEmail = itemView.findViewById(R.id.user_item_email)
    }

    fun bind(userItem: UserItem) {
        userItemDisplayName?.text = userItem.displayName
        userItemEmail?.text = userItem.email
    }
}

data class UserItem(
    var id: String? = null,
    var email: String? = null,
    var displayName: String? = null
)
