package com.swago.seenthemlive.ui.screens.map

import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class MapViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {
    val mapItemsFlow: Flow<List<MapItem>> = flow {
        emit(firebaseRepository.getMapItems())
    }

    var uiState: MapUiState by mutableStateOf(MapUiState.Loading)

    fun load(geocoder: Geocoder) {
        uiState = MapUiState.Loading
        viewModelScope.launch {
            mapItemsFlow.collect { items ->
                val updatedItems = items.map { mapItem ->
                    async {
                        val name = mapItem.name ?: ""
                        suspendCancellableCoroutine { continuation ->
                            geocoder.getFromLocationName(
                                name,
                                1,
                                (mapItem.lat ?: 0.0) - 2.0,
                                (mapItem.long ?: 0.0) - 2.0,
                                (mapItem.lat ?: 0.0) + 2.0,
                                (mapItem.long ?: 0.0) + 2.0
                            ) { addresses ->
                                if (addresses.isNotEmpty()) {
                                    val first = addresses.first()
                                    continuation.resume(
                                        mapItem.copy(
                                            lat = first.latitude,
                                            long = first.longitude
                                        )
                                    )
                                } else {
                                    Log.e("MapFragment", "Address not found for: $name")
                                    continuation.resume(mapItem)
                                }
                            }
                        }
                    }
                }.awaitAll()
                uiState = MapUiState.Loaded(updatedItems)
            }
        }
    }
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
