package com.swago.seenthemlive.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.BaseFragment
import com.swago.seenthemlive.ui.common.UserItem
import com.swago.seenthemlive.ui.common.UserListAdapter

class FriendsFragment : BaseFragment() {

    private lateinit var friendsViewModel: FriendsViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var content: ConstraintLayout? = null

    private var friends = mutableListOf<UserItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        val friendsRecyclerView: RecyclerView = root.findViewById(R.id.friends_recycler_view)
        content = root.findViewById(R.id.profile_content)
        loading = root.findViewById(R.id.loading)

        friendsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserListAdapter(friends, object : UserListAdapter.UserSelectedListener {
                override fun selected(id: String) {
                    val intent = FriendProfileActivity.newIntent(context, id)
                    startActivity(intent)
                }
            })
        }

        friendsViewModel.friends.observe(viewLifecycleOwner, Observer {
            friends.clear()
            friends.addAll(it.filterNot { profile -> profile.id == userId })
            friendsRecyclerView.adapter?.notifyDataSetChanged()
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
        friendsViewModel.fetchFriends {
            content?.visibility = View.VISIBLE
            loading?.hide()
        }
    }
}