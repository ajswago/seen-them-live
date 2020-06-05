package com.swago.seenthemlive.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _userDisplayName = MutableLiveData<String>().apply {
        value = "First Last"
    }
    val userDisplayName: MutableLiveData<String> = _userDisplayName

    private val _userUsername = MutableLiveData<String>().apply {
        value = "usr"
    }
    val userUsername: MutableLiveData<String> = _userUsername

    private val _userEmail = MutableLiveData<String>().apply {
        value = "first.last@email.com"
    }
    val userEmail: MutableLiveData<String> = _userEmail

    private val _userConcertCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val userConcertCount: MutableLiveData<Int> = _userConcertCount

    private val _userBandCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val userBandCount: MutableLiveData<Int> = _userBandCount

    private val _userVenueCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val userVenueCount: MutableLiveData<Int> = _userVenueCount
}