package com.swago.seenthemlive.models

import java.util.Date

data class Artist(
    val id: String,
    val name: String,
    val lastShow: Date,
    val showCount: Int = 0
)
