package com.swago.seenthemlive.models

import java.util.Date

data class Show(
    val id: String,
    val artist: String,
    val venueName: String,
    val city: String,
    val state: String,
    val date: Date,
    val tourName: String? = null,
    val saved: Boolean = false
)