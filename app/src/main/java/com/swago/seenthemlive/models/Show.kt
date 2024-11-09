package com.swago.seenthemlive.models

import java.util.Date

data class Show (
    val venueName: String,
    val locationName: String,
    val date: Date,
    val tourName: String,
    val artist: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Show

        if (artist != other.artist) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = artist.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}