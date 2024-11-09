package com.swago.seenthemlive.models

import java.util.Date

data class GroupedShow (
    val venueName: String,
    val locationName: String,
    val date: Date,
    val artistList: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupedShow

        if (venueName != other.venueName) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = venueName.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}