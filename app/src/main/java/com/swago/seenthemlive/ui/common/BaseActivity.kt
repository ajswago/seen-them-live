package com.swago.seenthemlive.ui.common

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

}