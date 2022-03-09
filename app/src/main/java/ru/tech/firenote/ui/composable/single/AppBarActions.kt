package ru.tech.firenote.ui.composable.single

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.tech.firenote.R
import ru.tech.firenote.Utils.isOnline
import ru.tech.firenote.viewModel.MainViewModel

@Composable
fun NoteActions(
    viewModel: MainViewModel
) {
    val selectedModifier =
        Modifier
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
    val unselectedModifier = Modifier.padding(5.dp)
    val showFilter = remember { mutableStateOf(false) }

    IconButton(onClick = {
        viewModel.isDescendingFilter.value = !viewModel.isDescendingFilter.value
    }) {
        Icon(
            if (viewModel.isDescendingFilter.value) Icons.Filled.ArrowDropDown else Icons.Outlined.ArrowDropUp,
            "filter"
        )
    }
    IconButton(onClick = { showFilter.value = true }) {
        Icon(Icons.Filled.FilterAlt, null)
    }

    DropdownMenu(
        expanded = showFilter.value,
        onDismissRequest = { showFilter.value = false }
    ) {
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 0
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.title)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 0) Icons.Filled.TextSnippet else Icons.Outlined.TextSnippet,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 0) selectedModifier else unselectedModifier
        )
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 1
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.color)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 1) Icons.Filled.Palette else Icons.Outlined.Palette,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 1) selectedModifier else unselectedModifier
        )
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 2
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.date)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 2) Icons.Filled.CalendarToday else Icons.Outlined.CalendarToday,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 2) selectedModifier else unselectedModifier
        )
    }
}

@Composable
fun AlarmActions() {
    val showFilter = remember { mutableStateOf(false) }
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

@Composable
fun ProfileActions(onClick: () -> Unit) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    IconButton(onClick = {
        if (context.isOnline()) showDialog.value = true
        else Toast.makeText(context, R.string.noInternet, Toast.LENGTH_LONG).show()
    }) {
        Icon(Icons.Filled.Logout, null)
    }

    MaterialDialog(
        showDialog = showDialog,
        icon = Icons.Outlined.Logout,
        title = R.string.logOut,
        message = R.string.logoutMessage,
        confirmText = R.string.stay,
        dismissText = R.string.logOut,
        dismissAction = onClick,
        backHandler = {}
    )
}
