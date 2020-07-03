package com.swago.seenthemlive.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.swago.seenthemlive.api.setlistfm.Venue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Utils {
    
    companion object {
        fun formatVenueString(venue: Venue) : String {
            val venueStringBuilder = StringBuilder()
            venueStringBuilder.append(venue.name)
            venueStringBuilder.append(" ")
            venue.city.let {
                venueStringBuilder.append("(")
                val locationString = formatLocationString(venue)
                venueStringBuilder.append(locationString)
                venueStringBuilder.append(")")
            }
            return venueStringBuilder.toString()
        }

        fun formatLocationString(venue: Venue) : String {
            val venueStringBuilder = StringBuilder()
            venueStringBuilder.append(venue.city?.name)
            venue.city?.state.let {
                venueStringBuilder.append(", ")
                venueStringBuilder.append(venue.city?.state)
            }
            venue.city?.country?.name.let {
                venueStringBuilder.append(", ")
                venueStringBuilder.append(venue.city?.country?.name)
            }
            return venueStringBuilder.toString()
        }

        fun formatDateString(dateStr: String) : String {
            val date = LocalDate.parse(
                dateStr,
                DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
            )
            return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH))
        }

        fun getPixelsFromDp(dp: Float, context: Context) : Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics)
        }
    }
}