package com.swago.seenthemlive.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme

@Composable
fun TextAvatar(
    character: Char,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.primaryContainer
    Box(
        modifier = modifier
            .background(color, shape = CircleShape)
            .width(40.dp)
            .height(40.dp)
    ) {
        Text(
            text = character.toString(),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextAvatarPreview() {
    SeenThemLiveComposeTheme {
        TextAvatar('B')
    }
}
