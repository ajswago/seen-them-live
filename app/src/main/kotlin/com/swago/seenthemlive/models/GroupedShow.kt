package com.swago.seenthemlive.models

import java.util.Date

data class GroupedShow(
    val id: String,
    val venueName: String,
    val city: String,
    val state: String,
    val date: Date,
    val artists: List<String>
)