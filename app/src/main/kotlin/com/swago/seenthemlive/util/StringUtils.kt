package com.swago.seenthemlive.util

fun List<String>.formatCommaSeparatedString(maxDisplayed: Int): String {
    var artistListString = this.sorted().take(maxDisplayed).joinToString(", ")
    if (this.count() > maxDisplayed) {
        artistListString += " +${this.count() - maxDisplayed}"
    }
    return artistListString
}
