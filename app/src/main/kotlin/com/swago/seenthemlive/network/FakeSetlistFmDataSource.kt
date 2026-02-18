package com.swago.seenthemlive.network

object FakeSetlistFmDataSource {
    val setlistResponse = SetlistResponse(
        listOf(
            Setlist(
                artist = Artist(
                    mbid = "101",
                    name = "Metallica",
                ),
                venue = Venue(
                    city = City(
                        name = "East Rutherford",
                        state = "New Jersey"
                    ),
                    name = "Metlife Stadium"
                ),
                tour = Tour(name = "M72 World Tour"),
                id = "109",
                eventDate = "2023-08-06",
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
                            Song(name = "Whiplash"),
                            Song(name = "For Whom the Bell Tolls"),
                            Song(name = "Ride the Lightning")
                        )
                    ),
                    Set(
                        encore = 1,
                        song = listOf(Song(name = "Enter Sandman"))
                    )
                ))
            ),
            Setlist(
                artist = Artist(
                    mbid = "101",
                    name = "Metallica",
                ),
                venue = Venue(
                    city = City(
                        name = "East Rutherford",
                        state = "New Jersey"
                    ),
                    name = "Metlife Stadium"
                ),
                tour = Tour(name = "M72 World Tour"),
                id = "112",
                eventDate = "2023-08-04",
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
                            Song(name = "Whiplash"),
                            Song(name = "For Whom the Bell Tolls"),
                            Song(name = "Ride the Lightning"),
                            Song(name = "The Memory Remains")
                        )
                    ),
                    Set(
                        encore = 1,
                        song = listOf(Song(name = "Enter Sandman"))
                    )
                ))
            ),
            Setlist(
                artist = Artist(
                    mbid = "101",
                    name = "Metallica",
                ),
                venue = Venue(
                    city = City(
                        name = "East Rutherford",
                        state = "New Jersey"
                    ),
                    name = "Metlife Stadium"
                ),
                tour = Tour(name = "Worldwired Tour"),
                id = "201",
                eventDate = "2021-05-14",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf(
                            Song(name = "Master of Puppets")
                        )
                    )
                ))
            )
        )
    )

    val relatedShowsResponse = SetlistResponse(
        listOf(
            Setlist(
                artist = Artist(
                    mbid = "201",
                    name = "Pantera",
                ),
                venue = Venue(
                    city = City(
                        name = "East Rutherford",
                        state = "New Jersey"
                    ),
                    name = "Metlife Stadium"
                ),
                tour = Tour(name = "M72 World Tour"),
                id = "201",
                eventDate = "2023-08-06",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf()
                    ),
                ))
            ),
            Setlist(
                artist = Artist(
                    mbid = "202",
                    name = "Suicidal Tendencies",
                ),
                venue = Venue(
                    city = City(
                        name = "East Rutherford",
                        state = "New Jersey"
                    ),
                    name = "Metlife Stadium"
                ),
                tour = Tour(name = "M72 World Tour"),
                id = "112",
                eventDate = "2023-08-04",
                sets = Sets(set = listOf(
                    Set(
                        song = listOf()
                    ),
                ))
            ),
        )
    )
}
