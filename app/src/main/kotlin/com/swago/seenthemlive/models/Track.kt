package com.swago.seenthemlive.models

data class Track (
    val trackName: String,
    val trackNumber: Int? = null,
    val trackCount: Int? = null,
    val coverArtistName: String? = null,
    val isTapeTrack: Boolean = false
)
