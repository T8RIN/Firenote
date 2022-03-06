package ru.tech.firenote.ui.composable.single

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.NotificationImportant
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.TextSnippet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import ru.tech.firenote.R
import ru.tech.firenote.viewModel.MainViewModel

@Composable
fun NoteActions(
    viewModel: MainViewModel
) {
    IconButton(onClick = { viewModel.setFilter(true) }) {
        Icon(
            imageVector = Icons.Filled.FilterAlt,
            contentDescription = null
        )
    }
    DropdownMenu(
        expanded = viewModel.showFilter.value,
        onDismissRequest = { viewModel.setFilter(false) }
    ) {
        DropdownMenuItem(
            onClick = { viewModel.setFilter(false) /*TODO*/ },
            text = { Text(stringResource(R.string.title)) },
            leadingIcon = {
                Icon(Icons.Outlined.TextSnippet, null)
            })
        DropdownMenuItem(
            onClick = { viewModel.setFilter(false) /*TODO*/ },
            text = { Text(stringResource(R.string.color)) },
            leadingIcon = {
                Icon(Icons.Outlined.Palette, null)
            })
        DropdownMenuItem(
            onClick = { viewModel.setFilter(false) /*TODO*/ },
            text = { Text(stringResource(R.string.date)) },
            leadingIcon = {
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
        DropdownMenuItem(onClick = { showFilter.value = false /*TODO*/ }, text = {
            Text(
                stringResource(R.string.date)
            )
        }, leadingIcon = {
            Icon(Icons.Outlined.CalendarToday, null)
        })
    }
}
