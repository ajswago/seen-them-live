package com.swago.seenthemlive.ui.screens.shows

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.swago.seenthemlive.R
import com.swago.seenthemlive.models.Show
import com.swago.seenthemlive.models.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAppBar(
    uiState: ShowUiState,
    onBackClick: () -> Unit,
    showToggleSaved: Boolean,
    showEdit: Boolean,
    onToggleSaved: (String) -> Unit,
    onEditClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val enabled = uiState !is ShowUiState.Loading
    val show = (uiState as? ShowUiState.Loaded).let { it?.show }
    val showSaved = show?.saved ?: false
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.show_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_description)
                )
            }
        },
        actions = {
            if (showEdit) {
                IconButton(onClick = { onEditClicked() }, enabled = enabled) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.edit_button_description)
                    )
                }
            }
            if (showToggleSaved) {
                IconButton(onClick = { show?.let{onToggleSaved(it.id)} }, enabled = enabled) {
                    Icon(
                        imageVector = if (showSaved) { Icons.Outlined.BookmarkRemove } else { Icons.Outlined.BookmarkAdd },
                        contentDescription = if (showSaved) { stringResource(R.string.remove_button_description) } else { stringResource(R.string.add_button_description) }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingShowAppBarPreview() {
    ShowAppBar(
        uiState = ShowUiState.Loading,
        onBackClick = {},
        showToggleSaved = true,
        showEdit = false,
        onToggleSaved = {},
        onEditClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun UnsavedShowAppBarPreview() {
    val show = Show(
        id = "ID1",
        venueName = "The Fillmore Silver Spring",
        city = "Silver Spring",
        state = "MD",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        tourName = "Warp Speed Warriors",
        artist = "DragonForce",
        saved = false
    )
    val tracks = listOf(
        Track("Fury of the Storm", trackNumber = 1)
    )
    val linkedShows = listOf<Show>()
    ShowAppBar(
        uiState = ShowUiState.Loaded(
            show,
            tracks,
            linkedShows
        ),
        onBackClick = {},
        showToggleSaved = true,
        showEdit = false,
        onToggleSaved = {},
        onEditClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SavedShowAppBarPreview() {
    val show = Show(
        id = "ID1",
        venueName = "The Fillmore Silver Spring",
        city = "Silver Spring",
        state = "MD",
        date = SimpleDateFormat(
            "yyyy-MM-dd", Locale.US
        ).parse("2024-04-09") ?: Date(),
        tourName = "Warp Speed Warriors",
        artist = "DragonForce",
        saved = true
    )
    val tracks = listOf(
        Track("Fury of the Storm", trackNumber = 1)
    )
    val linkedShows = listOf<Show>()
    ShowAppBar(
        uiState = ShowUiState.Loaded(
            show,
            tracks,
            linkedShows
        ),
        onBackClick = {},
        showToggleSaved = true,
        showEdit = true,
        onToggleSaved = {},
        onEditClicked = {}
    )
}
