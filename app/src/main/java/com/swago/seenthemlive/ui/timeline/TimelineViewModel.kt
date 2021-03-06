package com.swago.seenthemlive.ui.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimelineViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Timeline Coming Soon!"
    }
    val text: LiveData<String> = _text
}