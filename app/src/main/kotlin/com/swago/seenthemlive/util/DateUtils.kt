package com.swago.seenthemlive.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.formatForDisplay(): String {
    return SimpleDateFormat("M/d/yyyy", Locale.US).format(this)
}
