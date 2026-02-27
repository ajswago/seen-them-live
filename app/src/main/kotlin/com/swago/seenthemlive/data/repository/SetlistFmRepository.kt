package com.swago.seenthemlive.data.repository

import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import com.swago.seenthemlive.network.Setlist
import com.swago.seenthemlive.network.SetlistFmApiService
import com.swago.seenthemlive.network.SetlistResponse
import com.swago.seenthemlive.ui.components.cards.SearchTerms
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface SetlistFmRepository {
    suspend fun getSearchResults(searchTerms: SearchTerms): List<Show>
    suspend fun getSearchResults(date: Date, venue: String): List<Show>
    suspend fun getShow(showId: String): ShowData
}

class NetworkSetlistFmRepository @Inject constructor(
    private val setlistFmApiService: SetlistFmApiService
) : SetlistFmRepository {
    override suspend fun getSearchResults(searchTerms: SearchTerms): List<Show> =
        setlistFmApiService.getSearchResults(searchTerms = searchTerms).asShowsList()

    override suspend fun getSearchResults(date: Date, venue: String): List<Show> =
        setlistFmApiService.getSearchResults(date = date, venue = venue).asShowsList()

    override suspend fun getShow(showId: String): ShowData {
        val setlist = setlistFmApiService.getSetlist(id = showId)
        return ShowData(
            setlist.asShow(),
            setlist.asTracksList(),
            setlist.asEncoreTracksList()
        )
    }
}

fun SetlistResponse.asShowsList(): List<Show> {
    return this.setlist.mapNotNull { setlist ->
        if (listOf(setlist.id, setlist.venue, setlist.eventDate, setlist.artist).any { it == null }) {
            return@mapNotNull null
        }
        setlist.asShow()
    }
}

fun Setlist.asShow(): Show {
    val date = try {
        SimpleDateFormat(
            "dd-MM-yyyy", Locale.US
        ).parse(this.eventDate ?: "")
    } catch (_: ParseException) {
        Date()
    }
    return Show(
        id = this.id ?: "",
        venueName = this.venue?.name ?: "",
        city = this.venue?.city?.name ?: "",
        state = this.venue?.city?.state ?: "",
        date = date,
        tourName = this.tour?.name,
        artist = this.artist?.name ?: ""
    )
}

fun Setlist.asTracksList(): List<Track> {
    val nonEncoreSets = this.sets?.set?.filter{
        (it.encore ?: 0) <= 0
    }
    val nonTapeTracks = nonEncoreSets?.flatMap{ set -> set.song?.filter { !(it.tape ?: false) } ?: listOf() } ?: listOf()
    return nonEncoreSets?.flatMap { set -> set.song?.map { song ->
        val indexOf = nonTapeTracks.indexOf(song)
        Track(
            trackName = song.name ?: "",
            trackNumber = if (indexOf >= 0) indexOf + 1 else null,
            coverArtistName = song.cover?.name,
            isTapeTrack = song.tape ?: false
        )
    } ?: listOf() } ?: listOf()
}

fun Setlist.asEncoreTracksList(): List<Track> {
    return this.sets?.set?.filter{
        (it.encore ?: 0) > 0
    }?.flatMap { set -> set.song?.mapIndexed { index, song ->
        Track(
            trackName = song.name ?: "",
            trackNumber = index + 1,
            coverArtistName = song.cover?.name,
            isTapeTrack = song.tape ?: false
        )
    } ?: listOf() } ?: listOf()
}

data class ShowData(
    val show: Show,
    val tracks: List<Track>,
    val encore: List<Track>,
)
