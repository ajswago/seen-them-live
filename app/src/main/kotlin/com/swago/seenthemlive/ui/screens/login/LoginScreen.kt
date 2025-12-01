package com.swago.seenthemlive.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swago.seenthemlive.R

@Composable
fun LoginRoute(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()
    LoginScreen(
        onLogin = onLogin,
        modifier = modifier,
        loading = viewModel.loading,
        isOffline = isOffline
    )
}

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    isOffline: Boolean = false
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(140.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Seen Them Live",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(60.dp))
                ElevatedButton(
                    onClick = { onLogin() },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    enabled = !loading && !isOffline,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_google),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
                }
                Spacer(modifier = Modifier.height(80.dp))
                if (isOffline) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Error,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "Warning"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Not connected")
                    }
                } else if (loading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLogin = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreen(
        onLogin = {},
        loading = true
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenOfflinePreview() {
    LoginScreen(
        onLogin = {},
        isOffline = true
    )
}