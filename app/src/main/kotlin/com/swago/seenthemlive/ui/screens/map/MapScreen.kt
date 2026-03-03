package com.swago.seenthemlive.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.swago.seenthemlive.ui.components.AppBarWithProfile
import com.swago.seenthemlive.ui.components.ProfileMenuItem

@Composable
fun MapRoute(
    onProfileMenuOption: (ProfileMenuItem) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MapScreen(
        uiState = uiState,
        onProfileMenuOption = onProfileMenuOption,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    uiState: MapUiState,
    onProfileMenuOption: (ProfileMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppBarWithProfile(
                "Map",
                onProfileMenuOption = onProfileMenuOption
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            when(uiState) {
                MapUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is MapUiState.Loaded -> {
                    val mapItems = uiState.mapItems
                    val cameraPositionState = rememberCameraPositionState {
                        val minLat = mapItems.mapNotNull{ it.lat }.minOrNull() ?: 0.0
                        val maxLat = mapItems.mapNotNull{ it.lat }.maxOrNull() ?: 0.0
                        val minLong = mapItems.mapNotNull{ it.long }.minOrNull() ?: 0.0
                        val maxLong = mapItems.mapNotNull{ it.long }.maxOrNull() ?: 0.0
                        position = CameraPosition.fromLatLngZoom(
                            LatLng(
                                (maxLat + minLat) / 2,
                                (maxLong + minLong) / 2
                            ),
                            1.0f)
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        for (mapItem in mapItems) {
                            if (mapItem.lat != null && mapItem.long != null) {
                                val loc = LatLng(mapItem.lat, mapItem.long)
                                val state = rememberUpdatedMarkerState(position = loc)
                                Marker(state = state, title = "${mapItem.name} (${mapItem.count} Concerts)")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MapScreen(
        uiState = MapUiState.Loaded(listOf()),
        onProfileMenuOption = {}
    )
}
