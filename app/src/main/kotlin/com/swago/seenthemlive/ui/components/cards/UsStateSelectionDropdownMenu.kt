package com.swago.seenthemlive.ui.components.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swago.seenthemlive.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsStateSelectionDropdownMenu(
    selectedText: String?,
    onSelectionChange: ((UsState) -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(vertical = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = enabled && expanded,
            onExpandedChange = {
                if (enabled) {
                    expanded = !expanded
                }
            },
        ) {
            TextField(
                enabled = enabled,
                value = selectedText ?: stringResource(R.string.state_no_selection_label),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                label = {
                    Text(stringResource(R.string.state_field_label))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                UsState.entries.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.stateName ?: stringResource(R.string.state_no_selection_label)) },
                        onClick = {
                            onSelectionChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

enum class UsState(val stateName: String?, val code: String?) {
    NONE(null, null),
    ALABAMA("Alabama","AL"),
    ALASKA("Alaska","AK"),
    ARIZONA("Arizona","AZ"),
    ARKANSAS("Arkansas","AR"),
    CALIFORNIA("California","CA"),
    COLORADO("Colorado","CO"),
    CONNECTICUT("Connecticut","CT"),
    DELAWARE("Delaware","DE"),
    DC("District of Columbia","DC"),
    FLORIDA("Florida","FL"),
    GEORGIA("Georgia","GA"),
    HAWAII("Hawaii","HI"),
    IDAHO("Idaho","ID"),
    ILLINOIS("Illinois","IL"),
    INDIANA("Indiana","IN"),
    IOWA("Iowa","IA"),
    KANSAS("Kansas","KS"),
    KENTUCKY("Kentucky","KY"),
    LOUISIANA("Louisiana","LA"),
    MAINE("Maine","ME"),
    MARYLAND("Maryland","MD"),
    MASSACHUSETTS("Massachusetts","MA"),
    MICHIGAN("Michigan","MI"),
    MINNESOTA("Minnesota","MN"),
    MISSISSIPPI("Mississippi","MS"),
    MISSOURI("Missouri","MO"),
    MONTANA("Montana","MT"),
    NEBRASKA("Nebraska","NE"),
    NEVADA("Nevada","NV"),
    NEW_HAMPSHIRE("New Hampshire","NH"),
    NEW_JERSEY("New Jersey","NJ"),
    NEW_MEXICO("New Mexico","NM"),
    NEW_YORK("New York","NY"),
    NORTH_CAROLINA("North Carolina","NC"),
    NORTH_DAKOTA("North Dakota","ND"),
    OHIO("Ohio","OH"),
    OKLAHOMA("Oklahoma","OK"),
    OREGON("Oregon","OR"),
    PENNSYLVANIA("Pennsylvania","PA"),
    RHODE_ISLAND("Rhode Island","RI"),
    SOUTH_CAROLINA("South Carolina","SC"),
    SOUTH_DAKOTA("South Dakota","SD"),
    TENNESSEE("Tennessee","TN"),
    TEXAS("Texas","TX"),
    UTAH("Utah","UT"),
    VERMONT("Vermont","VT"),
    VIRGINIA("Virginia","VA"),
    WASHINGTON("Washington","WA"),
    WEST_VIRGINIA("West Virginia","WV"),
    WISCONSIN("Wisconsin","WI"),
    WYOMING("Wyoming","WY"),
}

@Preview(showBackground = true)
@Composable
fun UsStateSelectionDropdownMenuPreview() {
    var usState by remember { mutableStateOf(UsState.NONE) }
    UsStateSelectionDropdownMenu(
        selectedText = usState.stateName,
        onSelectionChange = { usState = it }
    )
}
