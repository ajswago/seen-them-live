package com.swago.seenthemlive.data.repository

import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Profile
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.network.FirebaseApiService
import com.swago.seenthemlive.network.Setlist
import com.swago.seenthemlive.network.UserData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.filter

interface FirebaseRepository {
    suspend fun getShows(): List<Show>
    suspend fun getArtists(): List<Artist>
    suspend fun getArtist(artistId: String): Artist
    suspend fun getShowsForArtist(artistId: String): List<GroupedShow>
    suspend fun getTracksForArtist(artistId: String): List<Track>
    suspend fun getShow(showId: String): Show
    suspend fun getTracksForShow(showId: String): List<Track>
    suspend fun getEncoreTracksForShow(showId: String): List<Track>
    suspend fun getLinkedShows(showId: String): List<Show>
    suspend fun getProfile(): Profile
    suspend fun getTopArtistsForProfile(): List<Artist>
}

class NetworkFirebaseRepository @Inject constructor(
    private val firebaseApiService: FirebaseApiService
) : FirebaseRepository {
    override suspend fun getShows(): List<Show> = firebaseApiService.getUser().getShows()
    override suspend fun getArtists(): List<Artist> = firebaseApiService.getUser().getArtists()
    override suspend fun getArtist(artistId: String): Artist = firebaseApiService.getUser().getArtist(artistId)
    override suspend fun getShowsForArtist(artistId: String): List<GroupedShow> = firebaseApiService.getUser().getShowsForArtist(artistId)
    override suspend fun getTracksForArtist(artistId: String): List<Track> = firebaseApiService.getUser().getTracksForArtist(artistId)
    override suspend fun getShow(showId: String): Show = firebaseApiService.getUser().getShow(showId)
    override suspend fun getTracksForShow(showId: String): List<Track> = firebaseApiService.getUser().getTracksForShow(showId)
    override suspend fun getEncoreTracksForShow(showId: String): List<Track> = firebaseApiService.getUser().getEncoreTracksForShow(showId)
    override suspend fun getLinkedShows(showId: String): List<Show> = firebaseApiService.getUser().getLinkedShows(showId)
    override suspend fun getProfile(): Profile = firebaseApiService.getUser().getProfile()
    override suspend fun getTopArtistsForProfile(): List<Artist> = firebaseApiService.getUser().getTopArtistsForProfile()
}

fun UserData.getShows(): List<Show> {
    return this.setlists?.mapNotNull { setlist ->
        if (listOf(setlist.id, setlist.venue, setlist.eventDate, setlist.artist).any { it == null }) {
            return@mapNotNull null
        }
        setlist.asShow()
    } ?: listOf()
}

fun UserData.getArtists(): List<Artist> {
    return this.setlists
        ?.groupBy { it.artist }
        ?.mapNotNull { groupedSetlist ->
            if (listOf(groupedSetlist.key?.mbid, groupedSetlist.key?.name).any { it == null }) {
                return@mapNotNull null
            }
            val sortedSetlists = groupedSetlist.value.sortedByDescending { setlist -> setlist.eventDate }
            val lastSetlistDate = sortedSetlists.first().eventDate
            Artist(
                id = groupedSetlist.key?.mbid ?: "",
                name = groupedSetlist.key?.name ?: "",
                lastShow = SimpleDateFormat(
                    "yyyy-MM-dd", Locale.US
                ).parse(lastSetlistDate ?: "") ?: Date(),
                showCount = groupedSetlist.value.size,
            )
        } ?: listOf()
}

fun UserData.getArtist(artistId: String): Artist {
    val groupedSetlists = this.setlists?.groupBy { it.artist }
    val artist = groupedSetlists?.keys?.first { it?.mbid == artistId }
    val sortedSetlists = groupedSetlists?.get(artist)?.sortedByDescending { setlist -> setlist.eventDate }
    val lastSetlistDate = sortedSetlists?.first()?.eventDate
    return Artist(
        id = artist?.mbid ?: "",
        name = artist?.name ?: "",
        lastShow = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse(lastSetlistDate ?: "") ?: Date(),
        showCount = sortedSetlists?.size ?: 0,
    )
}

fun UserData.getShowsForArtist(artistId: String): List<GroupedShow> {
    val setlistsByDate = this.setlists?.groupBy { Pair(it.eventDate, it.venue?.id) }
    val groupedSetlists = this.setlists?.groupBy { it.artist }
    val artist = groupedSetlists?.keys?.first { it?.mbid == artistId }
    val artistSetlists = groupedSetlists?.get(artist)
    return artistSetlists?.map { setlist ->
        GroupedShow(
            id = setlist.id ?: "",
            venueName = setlist.venue?.name ?: "",
            city = setlist.venue?.city?.name ?: "",
            state = setlist.venue?.city?.state ?: "",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse(setlist.eventDate ?: "") ?: Date(),
            artists = setlistsByDate?.get(Pair(setlist.eventDate, setlist.venue?.id))
                ?.filterNot { it.artist?.mbid == setlist.artist?.mbid }?.map{ it.artist?.name ?: "" } ?: listOf()
        )
    } ?: listOf()
}

fun UserData.getShow(showId: String): Show {
    val setlist = this.setlists?.first { it.id == showId } ?: Setlist()
    return setlist.asShow()
}

fun UserData.getTracksForArtist(artistId: String): List<Track> {
    val setlists = this.setlists?.groupBy {
        it.artist?.mbid
    }?.get(artistId)
    val allSongs =  setlists?.flatMap { setlist -> setlist.sets?.set?.flatMap { set -> set.song?.map { it } ?: ArrayList() } ?: ArrayList() }
    val songsWithoutTapes = allSongs?.filter { it.tape?.not() ?: true }
    return songsWithoutTapes?.groupBy { it.name }?.map {
        Track(
            trackName = it.key ?: "",
            trackCount = it.value.size
        )
    } ?: listOf()
}

fun UserData.getTracksForShow(showId: String): List<Track> {
    val setlist = this.setlists?.first { it.id == showId } ?: Setlist()
    return setlist.asTracksList()
}

fun UserData.getEncoreTracksForShow(showId: String): List<Track> {
    val setlist = this.setlists?.first { it.id == showId } ?: Setlist()
    return setlist.asEncoreTracksList()
}

fun UserData.getLinkedShows(showId: String): List<Show> {
    val setlistsByDate = this.setlists?.groupBy { Pair(it.eventDate, it.venue?.id) }
    val setlist = this.setlists?.first { it.id == showId } ?: Setlist()
    return setlistsByDate?.get(Pair(setlist.eventDate, setlist.venue?.id))
        ?.filterNot { it.artist?.mbid == setlist.artist?.mbid }?.map{ it.asShow() } ?: listOf()
}

fun UserData.getProfile(): Profile {
    return Profile(
        name = this.displayName ?: "",
        email = this.email ?: "",
        showCount = this.setlists?.groupBy {
            Pair(it.eventDate, it.venue?.id)
        }?.size ?: 0,
        venueCount = this.setlists?.groupBy {
            it.venue?.id
        }?.size ?: 0,
        artistCount = this.setlists?.groupBy {
            it.artist?.mbid
        }?.size ?: 0
    )
}

fun UserData.getTopArtistsForProfile(): List<Artist> {
    return this.setlists
        ?.groupBy { it.artist }
        ?.map {
            Artist(
                id = it.key?.mbid ?: "",
                name = it.key?.name ?: "",
                showCount = it.value.size,
                lastShow = Date()
            )
        } ?: listOf()
}
