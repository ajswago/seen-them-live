package com.swago.seenthemlive.data.repository

import com.swago.seenthemlive.models.Artist
import com.swago.seenthemlive.models.GroupedShow
import com.swago.seenthemlive.models.Profile
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.network.FirebaseApiService
import com.swago.seenthemlive.network.Setlist
import com.swago.seenthemlive.network.UserData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.filter

interface FirebaseRepository {
    suspend fun getShows(uid: String): List<Show>
    suspend fun getArtists(uid: String): List<Artist>
    suspend fun getArtist(uid: String, artistId: String): Artist
    suspend fun getShowsForArtist(uid: String, artistId: String): List<GroupedShow>
    suspend fun getTracksForArtist(uid: String, artistId: String): List<Track>
    suspend fun getShow(uid: String, showId: String): Show
    suspend fun showSaved(uid: String, showId: String): Boolean
    suspend fun getTracksForShow(uid: String, showId: String): List<Track>
    suspend fun getEncoreTracksForShow(uid: String, showId: String): List<Track>
    suspend fun getLinkedShows(uid: String, showId: String): List<Show>
    suspend fun getProfile(uid: String): Profile
    suspend fun getTopArtistsForProfile(uid: String): List<Artist>
    suspend fun toggleSaved(showId: String): Boolean
}

class NetworkFirebaseRepository @Inject constructor(
    private val firebaseApiService: FirebaseApiService
) : FirebaseRepository {
    override suspend fun getShows(uid: String): List<Show> = firebaseApiService.getUser(uid).getShows()
    override suspend fun getArtists(uid: String): List<Artist> = firebaseApiService.getUser(uid).getArtists()
    override suspend fun getArtist(uid: String, artistId: String): Artist = firebaseApiService.getUser(uid).getArtist(artistId)
    override suspend fun getShowsForArtist(uid: String, artistId: String): List<GroupedShow> = firebaseApiService.getUser(uid).getShowsForArtist(artistId)
    override suspend fun getTracksForArtist(uid: String, artistId: String): List<Track> = firebaseApiService.getUser(uid).getTracksForArtist(artistId)
    override suspend fun getShow(uid: String, showId: String): Show = firebaseApiService.getUser(uid).getShow(showId)
    override suspend fun showSaved(uid: String, showId: String): Boolean = firebaseApiService.getUser(uid).showSaved(showId)
    override suspend fun getTracksForShow(uid: String, showId: String): List<Track> = firebaseApiService.getUser(uid).getTracksForShow(showId)
    override suspend fun getEncoreTracksForShow(uid: String, showId: String): List<Track> = firebaseApiService.getUser(uid).getEncoreTracksForShow(showId)
    override suspend fun getLinkedShows(uid: String, showId: String): List<Show> = firebaseApiService.getUser(uid).getLinkedShows(showId)
    override suspend fun getProfile(uid: String): Profile = firebaseApiService.getUser(uid).getProfile()
    override suspend fun getTopArtistsForProfile(uid: String): List<Artist> = firebaseApiService.getUser(uid).getTopArtistsForProfile()
    override suspend fun toggleSaved(showId: String): Boolean = firebaseApiService.toggleSaved(showId).showSaved(showId)
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
            val date = try {
                SimpleDateFormat(
                    "dd-MM-yyyy", Locale.US
                ).parse(lastSetlistDate ?: "")
            } catch (e: ParseException) {
                Date()
            }
            Artist(
                id = groupedSetlist.key?.mbid ?: "",
                name = groupedSetlist.key?.name ?: "",
                lastShow = date,
                showCount = groupedSetlist.value.size,
            )
        } ?: listOf()
}

fun UserData.getArtist(artistId: String): Artist {
    val groupedSetlists = this.setlists?.groupBy { it.artist }
    val artist = groupedSetlists?.keys?.first { it?.mbid == artistId }
    val sortedSetlists = groupedSetlists?.get(artist)?.sortedByDescending { setlist -> setlist.eventDate }
    val lastSetlistDate = sortedSetlists?.first()?.eventDate
    val date = try {
        SimpleDateFormat(
            "dd-MM-yyyy", Locale.US
        ).parse(lastSetlistDate ?: "")
    } catch (e: ParseException) {
        Date()
    }
    return Artist(
        id = artist?.mbid ?: "",
        name = artist?.name ?: "",
        lastShow = date,
        showCount = sortedSetlists?.size ?: 0,
    )
}

fun UserData.getShowsForArtist(artistId: String): List<GroupedShow> {
    val setlistsByDate = this.setlists?.groupBy { Pair(it.eventDate, it.venue?.id) }
    val groupedSetlists = this.setlists?.groupBy { it.artist }
    val artist = groupedSetlists?.keys?.first { it?.mbid == artistId }
    val artistSetlists = groupedSetlists?.get(artist)
    return artistSetlists?.map { setlist ->
        val date = try {
            SimpleDateFormat(
                "dd-MM-yyyy", Locale.US
            ).parse(setlist.eventDate ?: "")
        } catch (e: ParseException) {
            Date()
        }
        GroupedShow(
            id = setlist.id ?: "",
            venueName = setlist.venue?.name ?: "",
            city = setlist.venue?.city?.name ?: "",
            state = setlist.venue?.city?.state ?: "",
            date = date,
            artists = setlistsByDate?.get(Pair(setlist.eventDate, setlist.venue?.id))
                ?.filterNot { it.artist?.mbid == setlist.artist?.mbid }?.map{ it.artist?.name ?: "" } ?: listOf()
        )
    } ?: listOf()
}

fun UserData.getShow(showId: String): Show {
    val setlist = this.setlists?.first { it.id == showId } ?: Setlist()
    return setlist.asShow()
}

fun UserData.showSaved(showId: String): Boolean {
    val setlist = this.setlists?.firstOrNull { it.id == showId }
    return setlist != null
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
