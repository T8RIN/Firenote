package ru.tech.firenote

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource

@Composable
fun NoteActions(showViewMenu: MutableState<Boolean>, showFilter: MutableState<Boolean>) {
    IconButton(onClick = { showFilter.value = true }) {
        Icon(
            imageVector = Icons.Filled.FilterAlt,
            contentDescription = null
        )
    }
    IconButton(onClick = { showViewMenu.value = true }) {
        Icon(
            imageVector = Icons.Filled.ViewCompact,
            contentDescription = null
        )
    }
    DropdownMenu(
        expanded = showViewMenu.value,
        onDismissRequest = { showViewMenu.value = false }
    ) {
        DropdownMenuItem(
            onClick = { showViewMenu.value = false /*TODO*/ },
            text = { Text(stringResource(R.string.viewList)) },
            leadingIcon = {
                Icon(Icons.Outlined.ViewList, null)
            })
        DropdownMenuItem(
            onClick = { showViewMenu.value = false /*TODO*/ },
            text = { Text(stringResource(R.string.viewCompact)) },
            leadingIcon = {
                Icon(Icons.Outlined.ViewCompact, null)
            })
        DropdownMenuItem(
            onClick = { showViewMenu.value = false /*TODO*/ },
            text = { Text(stringResource(R.string.viewGrid)) },
            leadingIcon = {
                Icon(Icons.Outlined.GridView, null)
            })
    }
    DropdownMenu(
        expanded = showFilter.value,
        onDismissRequest = { showFilter.value = false }
    ) {
        DropdownMenuItem(
            onClick = { showFilter.value = false /*TODO*/ },
            text = { Text(stringResource(R.string.color)) },
            leadingIcon = {
                Icon(Icons.Outlined.Palette, null)
            })
        DropdownMenuItem(onClick = { showFilter.value = false /*TODO*/ }, text = { Text(
            stringResource(R.string.date)
        ) }, leadingIcon = {
            Icon(Icons.Outlined.CalendarToday, null)
        })
    }
}

@Composable
fun AlarmActions(showFilter: MutableState<Boolean>) {
    IconButton(onClick = { showFilter.value = true }) {
        Icon(
            imageVector = Icons.Filled.FilterAlt,
            contentDescription = null
        )
    }
    DropdownMenu(
        expanded = showFilter.value,
        onDismissRequest = { showFilter.value = false }
    ) {
        DropdownMenuItem(
            onClick = { showFilter.value = false /*TODO*/ },
            text = { Text(stringResource(R.string.importance)) },
            leadingIcon = {
                Icon(Icons.Outlined.NotificationImportant, null)
            })
        DropdownMenuItem(onClick = { showFilter.value = false /*TODO*/ }, text = { Text(
            stringResource(R.string.date)
        ) }, leadingIcon = {
            Icon(Icons.Outlined.CalendarToday, null)
        })
    }
}