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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.theme.SeenThemLiveComposeTheme
import com.swago.seenthemlive.util.formatForDisplay
import com.swago.seenthemlive.util.shimmerLoading
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ArtistCard(
    artistName: String,
    lastShow: Date,
    showCount: Int,
    songCount: Int,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = artistName,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = String.format(
                    stringResource(R.string.last_show_date_format),
                    lastShow.formatForDisplay()),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = pluralStringResource(R.plurals.count_times_seen_format,
                    showCount, showCount),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = pluralStringResource(R.plurals.count_unique_songs_format,
                    songCount, songCount),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun LoadingArtistCard() {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
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
                    .width(200.dp)
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(200.dp)
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(200.dp)
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerLoading()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistCardPreview() {
    SeenThemLiveComposeTheme {
        ArtistCard(
            artistName = "Lamb of God",
            lastShow = SimpleDateFormat(
                "yyyy-MM-dd", Locale.US
            ).parse("2022-05-15") ?: Date(),
            showCount = 5,
            songCount = 25
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingArtistCardPreview() {
    SeenThemLiveComposeTheme {
        LoadingArtistCard()
    }
}
