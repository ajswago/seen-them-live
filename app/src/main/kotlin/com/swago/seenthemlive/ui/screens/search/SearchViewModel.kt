package com.swago.seenthemlive.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.SetlistFmRepository
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.ui.components.cards.SearchTerms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val setlistFmRepository: SetlistFmRepository
) : ViewModel() {

    var uiState: SearchUiState by mutableStateOf(SearchUiState.Empty)

    fun performSearch(terms: SearchTerms) {
        uiState = SearchUiState.Loading
        viewModelScope.launch {
            delay(Duration.ofMillis(2000))
            uiState = SearchUiState.Results(shows = setlistFmRepository.getSearchResults(terms))
        }
    }
}

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Results(
        val shows: List<Show>
    ) : SearchUiState
    data object Empty : SearchUiState
}
