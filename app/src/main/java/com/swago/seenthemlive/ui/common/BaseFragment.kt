package com.swago.seenthemlive.ui.common

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth

open class BaseFragment : Fragment() {

    val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun nonScrollingLayoutManager() : LinearLayoutManager {
        return object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }
}