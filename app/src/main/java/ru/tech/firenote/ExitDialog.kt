package ru.tech.firenote

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun ExitDialog(
    icon: ImageVector,
    title: String,
    message: String,
    confirmText: String,
    confirmAction: () -> Unit = {},
    dismissText: String,
    dismissAction: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val showExitDialog = remember { mutableStateOf(false) }

    if (showExitDialog.value) {
        AlertDialog(
            icon = { Icon(icon, contentDescription = null) },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = {
                    confirmAction()
                    showExitDialog.value = false
                }) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dismissAction()
                    showExitDialog.value = false
                }) {
                    Text(dismissText)
                }
            },
            onDismissRequest = { showExitDialog.value = false }
        )
    } else {
        onDismiss()
    }

    BackHandler { showExitDialog.value = true }

}


@Composable
fun ExitDialog(
    icon: ImageVector,
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes confirmText: Int,
    confirmAction: () -> Unit = {},
    @StringRes dismissText: Int,
    dismissAction: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val showExitDialog = remember { mutableStateOf(false) }

    if (showExitDialog.value) {
        AlertDialog(
            icon = { Icon(icon, contentDescription = null) },
            title = { Text(stringResource(title)) },
            text = { Text(stringResource(message)) },
            confirmButton = {
                TextButton(onClick = {
                    confirmAction()
                    showExitDialog.value = false
                }) {
                    Text(stringResource(confirmText))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dismissAction()
                    showExitDialog.value = false
                }) {
                    Text(stringResource(dismissText))
                }
            },
            onDismissRequest = { showExitDialog.value = false }
        )
    } else {
        onDismiss()
    }

    BackHandler { showExitDialog.value = true }

}