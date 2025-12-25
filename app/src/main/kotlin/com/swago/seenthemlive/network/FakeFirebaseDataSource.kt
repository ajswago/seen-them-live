package com.swago.seenthemlive.network

import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Profile
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.io.path.fileVisitor

object FakeFirebaseDataSource {

    val metallica = Artist(
        mbid = "101",
        name = "Metallica",
    )
    val pantera = Artist(
        mbid = "102",
        name = "Pantera",
    )
    val suicidaltendencies = Artist(
        mbid = "103",
        name = "Suicidal Tendencies",
    )
    val rodrigoygabriela = Artist(
        mbid = "104",
        name = "Rodrigo y Gabriela",
    )
    val joesatriani = Artist(
        mbid = "105",
        name = "Joe Satriani",
    )
    val stevevai = Artist(
        mbid = "106",
        name = "Steve Vai",
    )
    val dethklok = Artist(
        mbid = "107",
        name = "Dethklok",
    )
    val dragonforce = Artist(
        mbid = "108",
        name = "DragonForce",
    )
    val nekrogoblikon = Artist(
        mbid = "109",
        name = "Nekrogoblikon",
    )
    val fivefingerdeathpunch = Artist(
        mbid = "110",
        name = "Five Finger Death Punch",
    )
    val iceninekills = Artist(
        mbid = "111",
        name = "Ice Nine Kills",
    )
    val mammothwvh = Artist(
        mbid = "112",
        name = "Mammoth WVH",
    )
    val exodus = Artist(
        mbid = "113",
        name = "Exodus",
    )
    val testament = Artist(
        mbid = "114",
        name = "Testament",
    )

    val lanestadium = Venue(
        id = "101",
        name = "Lane Stadium",
        city = City(
            name = "Blacksburg",
            state = "Virginia"
        )
    )
    val capitalonehall = Venue(
        id = "102",
        name = "Capital One Hall",
        city = City(
            name = "Tysons Corner",
            state = "Virginia"
        )
    )
    val warnertheatre = Venue(
        id = "103",
        name = "Warner Theatre",
        city = City(
            name = "Washington",
            state = "Washington, D.C."
        )
    )
    val thefillmore = Venue(
        id = "104",
        name = "The Fillmore",
        city = City(
            name = "Silver Spring",
            state = "Maryland"
        )
    )
    val metlifestadium = Venue(
        id = "105",
        name = "Metlife Stadium",
        city = City(
            name = "East Rutherford",
            state = "New Jersey"
        )
    )
    val wolftrap = Venue(
        id = "106",
        name = "Filene Center at Wolf Trap",
        city = City(
            name = "Vienna",
            state = "Virginia"
        )
    )

    val user = UserData(
        id = "101",
        username = "hansolo",
        email = "han.solo@emailhost.com",
        displayName = "Han Solo",
        setlists = listOf(
            Setlist(
                id = "101",
                artist = metallica,
                venue = lanestadium,
                tour = Tour(name = "M72 World Tour"),
                eventDate = "2025-05-07",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(
                                name = "It's a Long Way to the Top (If You Wanna Rock 'n' Roll)",
                                cover = Artist(name = "AC/DC"),
                                tape = true
                            ),
                            Song(
                                name = "The Ecstasy of Gold",
                                cover = Artist(name = "Enrico Morricone"),
                                tape = true
                            ),
                            Song(name = "Creeping Death"),
                            Song(name = "For Whom the Bell Tolls"),
                            Song(name = "Ride the Lightning"),
                            Song(name = "King Nothing"),
                            Song(name = "Screaming Suicide"),
                            Song(name = "The Day That Never Comes"),
                            Song(name = "Fuel"),
                            Song(name = "Enter Sandman"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "102",
                artist = pantera,
                venue = lanestadium,
                eventDate = "2025-05-07",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "A New Level"),
                            Song(name = "Mouth For War"),
                            Song(name = "Strength Beyond Strength"),
                            Song(name = "Walk"),
                            Song(name = "Cowboys From Hell"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "103",
                artist = suicidaltendencies,
                venue = lanestadium,
                eventDate = "2025-05-07",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Institutionalized"),
                            Song(name = "Subliminal"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "104",
                artist = rodrigoygabriela,
                venue = capitalonehall,
                tour = Tour(name = "In Between Thoughts... A New World"),
                eventDate = "2024-04-25",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Tamacun"),
                            Song(name = "Hanuman"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "104",
                artist = joesatriani,
                venue = warnertheatre,
                eventDate = "2024-04-11",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "The Extremist"),
                            Song(name = "Surfing With the Alien"),
                            Song(name = "Satch Boogie"),
                            Song(name = "Sahara"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "105",
                artist = stevevai,
                venue = warnertheatre,
                eventDate = "2024-04-11",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Avalancha"),
                            Song(name = "Building the Church"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "106",
                artist = dethklok,
                venue = thefillmore,
                tour = Tour(name = "Mutilation On A Spring Night Tour"),
                eventDate = "2024-04-09",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Bloodlines"),
                            Song(name = "Awaken"),
                            Song(name = "The Gears"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "107",
                artist = dragonforce,
                venue = thefillmore,
                tour = Tour(name = "Warp Speed Warriors"),
                eventDate = "2024-04-09",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Fury of the Storm"),
                            Song(name = "Cry Thunder"),
                            Song(name = "Through the Fire and Flames"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "108",
                artist = nekrogoblikon,
                venue = thefillmore,
                eventDate = "2024-04-09",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Powercore"),
                            Song(name = "Darkness"),
                            Song(name = "No One Survives"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "109",
                artist = metallica,
                venue = metlifestadium,
                tour = Tour(name = "M72 World Tour"),
                eventDate = "2023-08-06",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Whiplash"),
                            Song(name = "For Whom the Bell Tolls"),
                            Song(name = "Ride the Lightning"),
                        )
                    ),
                    Set(
                        encore = 1,
                        song = listOf(
                            Song(name = "Enter Sandman"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "110",
                artist = fivefingerdeathpunch,
                venue = metlifestadium,
                eventDate = "2023-08-06",
                sets = Sets(set = listOf(
                ))
            ),
            Setlist(
                id = "111",
                artist = iceninekills,
                venue = metlifestadium,
                eventDate = "2023-08-06",
                sets = Sets(set = listOf(
                ))
            ),
            Setlist(
                id = "112",
                artist = metallica,
                venue = metlifestadium,
                tour = Tour(name = "M72 World Tour"),
                eventDate = "2023-08-04",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Creeping Death"),
                            Song(name = "King Nothing"),
                            Song(name = "Fade to Black"),
                        )
                    ),
                    Set(
                        encore = 1,
                        song = listOf(
                            Song(name = "Master of Puppets"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "113",
                artist = pantera,
                venue = metlifestadium,
                tour = Tour(name = "M72 World Tour"),
                eventDate = "2023-08-04",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Walk"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "114",
                artist = mammothwvh,
                venue = metlifestadium,
                tour = Tour(name = "M72 World Tour"),
                eventDate = "2023-08-04",
                sets = Sets(set = listOf(
                ))
            ),
            Setlist(
                id = "115",
                artist = exodus,
                venue = thefillmore,
                tour = Tour(name = "Dark Roots of Thrash II"),
                eventDate = "2015-04-28",
                sets = Sets(set = listOf(
                ))
            ),
            Setlist(
                id = "116",
                artist = testament,
                venue = thefillmore,
                tour = Tour(name = "Dark Roots of Thrash II"),
                eventDate = "2015-04-28",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Over the Wall"),
                            Song(name = "The Preacher"),
                        )
                    ),
                ))
            ),
            Setlist(
                id = "117",
                artist = rodrigoygabriela,
                venue = wolftrap,
                eventDate = "2014-07-31",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Tamacun")
                        )
                    )
                ))
            ),
        )
    )

//    val shows = listOf(
//        Show(
//            id="ID1",
//            venueName = "Capital One Hall",
//            city = "Tysons Corner",
//            state = "VA",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2022-06-10") ?: Date(),
//            artist="Rodrigo y Gabriela"
//        ),
//        Show(
//            id="ID2",
//            venueName = "Warner Theatre",
//            city = "Washington",
//            state = "DC",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2024-04-11") ?: Date(),
//            artist = "Joe Satriani"
//        ),
//        Show(
//            id="ID3",
//            venueName = "Warner Theatre",
//            city = "Washington",
//            state = "DC",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2024-04-11") ?: Date(),
//            artist = "Steve Vai"
//        ),
//        Show(
//            id = "ID4",
//            venueName = "The Fillmore Silver Spring",
//            city = "Silver Spring",
//            state = "MD",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2024-04-09") ?: Date(),
//            artist = "Dethklok"
//        ),
//        Show(
//            id = "ID5",
//            venueName = "The Fillmore Silver Spring",
//            city = "Silver Spring",
//            state = "MD",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2024-04-09") ?: Date(),
//            artist = "DragonForce"
//        ),
//        Show(
//            id = "ID6",
//            venueName = "The Fillmore Silver Spring",
//            city = "Silver Spring",
//            state = "MD",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2024-04-09") ?: Date(),
//            artist = "Nekrogoblikon"
//        )
//    )
//
//    val artists = listOf(
//        Artist(
//            id = "ID1",
//            name = "AC/DC",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2016-09-17") ?: Date(),
//            showCount = 1
//        ),
//        Artist(
//            id = "ID2",
//            name = "Children of Bodom",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2014-03-21") ?: Date(),
//            showCount = 3
//        ),
//        Artist(
//            id = "ID3",
//            name = "Five Finger Death Punch",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2023-08-06") ?: Date(),
//            showCount = 2
//        ),
//        Artist(
//            id = "ID4",
//            name = "Testament",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2018-06-10") ?: Date(),
//            showCount = 3
//        )
//    )
//
//    val artistInfo = Artist(
//        id = "ID1",
//        name = "Lamb of God",
//        lastShow = SimpleDateFormat(
//            "yyyy-MM-dd", Locale.US
//        ).parse("2022-06-10") ?: Date(),
//    )
//
//    val groupedShowsForArtist = listOf(
//        GroupedShow(
//            id = "ID1",
//            venueName = "Jiffy Lube Live",
//            city = "Bristow",
//            state = "VA",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2022-06-10") ?: Date(),
//            artists = arrayOf("Anthrax", "Behemoth", "Slayer", "Testament")
//        ),
//        GroupedShow(
//            id = "ID2",
//            venueName = "PPL Center",
//            city = "Allentown",
//            state = "PA",
//            date = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2022-05-15") ?: Date(),
//            arrayOf("Megadeth", "Trivium")
//        )
//    )
//
//    val tracksForArtist = listOf(
//        Track("Ruin", trackCount = 5),
//        Track("Walk With Me in Hell", trackCount = 5),
//        Track("Now You've Got Something to Die For", trackCount = 5),
//        Track("Laid to Rest", trackCount = 5),
//        Track("Redneck", trackCount = 3),
//        Track("512", trackCount = 3)
//    )
//
//    val showInfo = Show(
//        id = "ID1",
//        venueName = "The Fillmore Silver Spring",
//        city = "Silver Spring",
//        state = "MD",
//        date = SimpleDateFormat(
//            "yyyy-MM-dd", Locale.US
//        ).parse("2024-04-09") ?: Date(),
//        tourName = "Warp Speed Warriors",
//        artist = "DragonForce"
//    )
//
//    val tracksForShow = listOf(
//        Track("Fury of the Storm", trackNumber = 1),
//        Track("Cry Thunder", trackNumber = 2),
//        Track("Power of the Triforce", trackNumber = 3),
//        Track("The Last Dragonborn", trackNumber = 4),
//        Track("Doomsday Party", trackNumber = 5),
//        Track("My Heart Will Go On", trackNumber = 6, coverArtistName = "Celine Dion"),
//    )
//
//    val encoreTracksForShow = listOf(
//        Track("Through the Fire and Flames", trackNumber = 1)
//    )
//
//    val relatedShowsForShow = listOf(
//        Show(
//            id = "ID2",
//            artist = "Nekrogoblikon",
//            city = "Silver Spring",
//            state = "MD",
//            venueName = "The Fillmore Silver Spring",
//            date = Date()
//        ),
//        Show(
//            id = "ID3",
//            artist = "Dethklok",
//            city = "Silver Spring",
//            state = "MD",
//            venueName = "The Fillmore Silver Spring",
//            date = Date()
//        )
//    )
//
//    val profileInfo = Profile(
//        name = "Anthony Swago",
//        email = "ajswago@gmail.com",
//        showCount = 72,
//        venueCount = 34,
//        artistCount = 78
//    )
//
//    val topArtistsForProfile = listOf(
//        Artist(
//            "ID1",
//            name = "Lamb of God",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2022-05-15") ?: Date(),
//            showCount = 5
//        ),
//        Artist(
//            "ID2",
//            name = "Megadeth",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2022-05-15") ?: Date(),
//            showCount = 5
//        ),
//        Artist(
//            "ID3",
//            name = "Opeth",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2022-05-15") ?: Date(),
//            showCount = 5
//        ),
//        Artist(
//            "ID4",
//            name = "Iron Maiden",
//            lastShow = SimpleDateFormat(
//                "yyyy-MM-dd", Locale.US
//            ).parse("2022-05-15") ?: Date(),
//            showCount = 4
//        )
//    )
}
