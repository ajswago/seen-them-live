package com.swago.seenthemlive.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.swago.seenthemlive.BuildConfig
import com.swago.seenthemlive.data.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(BuildConfig.GOOGLE_WEB_API_KEY)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    fun performLogin(getCredential: suspend (GetCredentialRequest) -> GetCredentialResponse, completion: (FirebaseUser?) -> Unit) {
        uiState = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val result = getCredential(request)
                handleSignIn(result.credential, completion)
            } catch (e: GetCredentialException) {
                completion(null)
                uiState = LoginUiState.Failed
            }
        }
    }

    private fun handleSignIn(credential: Credential, completion: (FirebaseUser?) -> Unit) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken, completion)
        } else {
            completion(null)
            uiState = LoginUiState.Failed
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, completion: (FirebaseUser?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    completion(user)
                } else {
                    completion(null)
                    uiState = LoginUiState.Failed
                }
            }
    }
}

sealed interface LoginUiState {
    data object Ready : LoginUiState
    data object Loading : LoginUiState
    data object Failed: LoginUiState
}
