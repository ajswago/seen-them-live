package com.swago.seenthemlive.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {
    val mapItemsFlow: Flow<List<MapItem>> = flow {
        emit(firebaseRepository.getMapItems())
    }

    val uiState: StateFlow<MapUiState> =
        mapItemsFlow.map {
            MapUiState.Loaded(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = MapUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface MapUiState {
    data object Loading : MapUiState
    data class Loaded(
        val mapItems: List<MapItem>
    ) : MapUiState
}

data class MapItem(
    val name: String? = null,
    val count: Int? = null,
    val lat: Double? = null,
    val long: Double? = null
)
