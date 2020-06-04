package com.swago.seenthemlive.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swago.seenthemlive.api.setlistfm.Setlist

class ListViewModel : ViewModel() {

    private val _setlists = MutableLiveData<List<Setlist>>().apply {
        value = ArrayList()
    }
    val setlists: MutableLiveData<List<Setlist>> = _setlists

//    fun addAllSetlists(lists: List<Setlist>) {
//        setlists.postValue(lists)
//    }
}