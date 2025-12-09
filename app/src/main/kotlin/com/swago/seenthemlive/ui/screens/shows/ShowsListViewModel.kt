package com.swago.seenthemlive.ui.screens.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.time.delay
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ShowsListViewModel @Inject constructor() : ViewModel() {

    val showsFlow: Flow<Array<Show>> = flow {
        delay(Duration.ofMillis(2000))
        emit(shows)
    }

    val uiState: StateFlow<ShowsListUiState> =
        showsFlow.map {
            ShowsListUiState.Loaded(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = ShowsListUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val shows = arrayOf(
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
}

sealed interface ShowsListUiState {
    data object Loading : ShowsListUiState
    data class Loaded(
        val shows: Array<Show>
    ) : ShowsListUiState
}
