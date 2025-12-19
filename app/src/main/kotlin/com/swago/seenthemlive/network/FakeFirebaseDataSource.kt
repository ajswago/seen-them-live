package com.swago.seenthemlive.network

import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Profile
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FakeFirebaseDataSource {
    val shows = listOf(
        Show(
            id="ID1",
            venueName = "Capital One Hall",
            city = "Tysons Corner",
            state = "VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date(),
            artist="Rodrigo y Gabriela"
        ),
        Show(
            id="ID2",
            venueName = "Warner Theatre",
            city = "Washington",
            state = "DC",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-11") ?: Date(),
            artist = "Joe Satriani"
        ),
        Show(
            id="ID3",
            venueName = "Warner Theatre",
            city = "Washington",
            state = "DC",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-11") ?: Date(),
            artist = "Steve Vai"
        ),
        Show(
            id = "ID4",
            venueName = "The Fillmore Silver Spring",
            city = "Silver Spring",
            state = "MD",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-09") ?: Date(),
            artist = "Dethklok"
        ),
        Show(
            id = "ID5",
            venueName = "The Fillmore Silver Spring",
            city = "Silver Spring",
            state = "MD",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-09") ?: Date(),
            artist = "DragonForce"
        ),
        Show(
            id = "ID6",
            venueName = "The Fillmore Silver Spring",
            city = "Silver Spring",
            state = "MD",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2024-04-09") ?: Date(),
            artist = "Nekrogoblikon"
        )
    )

    val artists = listOf(
        Artist(
            id = "ID1",
            name = "AC/DC",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2016-09-17") ?: Date(),
            showCount = 1
        ),
        Artist(
            id = "ID2",
            name = "Children of Bodom",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2014-03-21") ?: Date(),
            showCount = 3
        ),
        Artist(
            id = "ID3",
            name = "Five Finger Death Punch",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            showCount = 2
        ),
        Artist(
            id = "ID4",
            name = "Testament",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2018-06-10") ?: Date(),
            showCount = 3
        )
    )

    val artistInfo = Artist(
        id = "ID1",
        name = "Lamb of God",
        lastShow = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2022-06-10") ?: Date(),
    )

    val groupedShowsForArtist = listOf(
        GroupedShow(
            id = "ID1",
            venueName = "Jiffy Lube Live",
            city = "Bristow",
            state = "VA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-06-10") ?: Date(),
            artists = arrayOf("Anthrax", "Behemoth", "Slayer", "Testament")
        ),
        GroupedShow(
            id = "ID2",
            venueName = "PPL Center",
            city = "Allentown",
            state = "PA",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            arrayOf("Megadeth", "Trivium")
        )
    )

    val tracksForArtist = listOf(
        Track("Ruin", trackCount = 5),
        Track("Walk With Me in Hell", trackCount = 5),
        Track("Now You've Got Something to Die For", trackCount = 5),
        Track("Laid to Rest", trackCount = 5),
        Track("Redneck", trackCount = 3),
        Track("512", trackCount = 3)
    )

    val showInfo = Show(
        id = "ID1",
        venueName = "The Fillmore Silver Spring",
        city = "Silver Spring",
        state = "MD",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        tourName = "Warp Speed Warriors",
        artist = "DragonForce"
    )

    val tracksForShow = listOf(
        Track("Fury of the Storm", trackNumber = 1),
        Track("Cry Thunder", trackNumber = 2),
        Track("Power of the Triforce", trackNumber = 3),
        Track("The Last Dragonborn", trackNumber = 4),
        Track("Doomsday Party", trackNumber = 5),
        Track("My Heart Will Go On", trackNumber = 6, coverArtistName = "Celine Dion"),
        Track("Through the Fire and Flames", trackNumber = 7)
    )

    val relatedShowsForShow = listOf(
        Show(
            id = "ID2",
            artist = "Nekrogoblikon",
            city = "Silver Spring",
            state = "MD",
            venueName = "The Fillmore Silver Spring",
            date = Date()
        ),
        Show(
            id = "ID3",
            artist = "Dethklok",
            city = "Silver Spring",
            state = "MD",
            venueName = "The Fillmore Silver Spring",
            date = Date()
        )
    )

    val profileInfo = Profile(
        name = "Anthony Swago",
        email = "ajswago@gmail.com",
        showCount = 72,
        venueCount = 34,
        artistCount = 78
    )

    val topArtistsForProfile = listOf(
        Artist(
            "ID1",
            name = "Lamb of God",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID2",
            name = "Megadeth",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID3",
            name = "Opeth",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5
        ),
        Artist(
            "ID4",
            name = "Iron Maiden",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 4
        )
    )
}
