package com.swago.seenthemlive.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swago.seenthemlive.data.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    networkMonitor: NetworkMonitor
) : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState.Ready)

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun performLogin(completion: () -> Unit) {
        uiState = LoginUiState.Loading
        viewModelScope.launch {
            delay(Duration.ofMillis(2000))
            completion()
        }
    }
}

sealed interface LoginUiState {
    data object Ready : LoginUiState
    data object Loading : LoginUiState
}
