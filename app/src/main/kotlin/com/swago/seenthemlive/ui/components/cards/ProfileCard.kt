package com.swago.seenthemlive.ui.components.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.shimmerLoading

@Composable
fun ProfileCard(
    profileName: String,
    email: String,
    showCount: Int,
    artistCount: Int,
    venueCount: Int,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = profileName,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = pluralStringResource(R.plurals.count_shows_format,
                    showCount, showCount),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = pluralStringResource(R.plurals.count_artists_format,
                    artistCount, artistCount),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = pluralStringResource(R.plurals.count_venues_format,
                    venueCount, venueCount),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun LoadingProfileCard() {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(240.dp)
                    .height(30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(100.dp)
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(100.dp)
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(100.dp)
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(100.dp)
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    SeenThemLiveComposeTheme {
        ProfileCard(
            profileName = "Anthony Swago",
            email = "ajswago@gmail.com",
            showCount = 67,
            artistCount = 1113,
            venueCount = 29
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingProfileCardPreview() {
    SeenThemLiveComposeTheme {
        LoadingProfileCard()
    }
}
