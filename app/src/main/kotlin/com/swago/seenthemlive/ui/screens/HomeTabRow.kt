package com.swago.seenthemlive.ui.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.models.Screen
import com.swago.seenthemlive.models.screens

@Composable
fun HomeTabRow(
    screens: List<Screen>,
    onTabSelected: (Screen) -> Unit,
    currentScreen: Screen
) {
    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.route) },
                label = { Text(screen.label) },
                onClick = { onTabSelected(screen) },
                selected = currentScreen == screen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTabRowPreview() {
    var screen by remember { mutableStateOf(screens[0]) }
    HomeTabRow(
        screens = screens,
        onTabSelected = { screen = it },
        currentScreen = screen
    )
}
