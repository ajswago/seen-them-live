package com.swago.seenthemlive.network

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
}
