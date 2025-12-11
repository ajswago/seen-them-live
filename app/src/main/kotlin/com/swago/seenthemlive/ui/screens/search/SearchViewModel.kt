package com.swago.seenthemlive.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    var uiState: SearchUiState by mutableStateOf(SearchUiState.Empty)

    fun performSearch() {
        uiState = SearchUiState.Loading
        viewModelScope.launch {
            delay(Duration.ofMillis(2000))
            uiState = SearchUiState.Results(shows = results)
        }
    }

    val results = arrayOf(
        Show(
            id = "ID1",
            venueName = "Metlife Stadium",
            city = "East Rutherford",
            state = "NJ",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-06") ?: Date(),
            tourName = "M72 World Tour",
            artist = "Metallica"
        ),
        Show(
            id = "ID2",
            venueName = "Metlife Stadium",
            city = "East Rutherford",
            state = "NJ",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-08-04") ?: Date(),
            tourName = "M72 World Tour",
            artist = "Metallica"
        ),
        Show(
            id = "ID3",
            venueName = "Metlife Stadium",
            city = "East Rutherford",
            state = "NJ",
            date = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2023-05-14") ?: Date(),
            tourName = "Worldwired Tour",
            artist = "Metallica"
        ),
    )
}

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Results(
        val shows: Array<Show>
    ) : SearchUiState
    data object Empty : SearchUiState
}
