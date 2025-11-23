package com.swago.seenthemlive.util

fun Array<String>.formatCommaSeparatedString(maxDisplayed: Int): String {
    var artistListString = this.take(maxDisplayed).joinToString(", ")
    if (this.count() > maxDisplayed) {
        artistListString += " +${this.count() - maxDisplayed}"
    }
    return artistListString
}
