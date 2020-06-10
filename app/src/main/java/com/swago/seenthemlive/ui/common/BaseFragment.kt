package com.swago.seenthemlive.ui.common

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

open class BaseFragment : Fragment() {

    val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
}