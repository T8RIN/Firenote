package ru.tech.firenote.ui.composable.single

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MaterialDialog(
    showDialog: MutableState<Boolean> = mutableStateOf(false),
    icon: ImageVector,
    title: String,
    message: String,
    confirmText: String,
    confirmAction: () -> Unit = {},
    dismissText: String,
    dismissAction: () -> Unit = {},
    onDismiss: () -> Unit = {},
    backHandler: @Composable () -> Unit = { BackHandler { showDialog.value = true } }
) {
    if (showDialog.value) {
        AlertDialog(
            icon = { Icon(icon, null) },
            title = { Text(title) },
            text = { Text(message, textAlign = TextAlign.Center) },
            confirmButton = {
                TextButton(onClick = {
                    confirmAction()
                    showDialog.value = false
                }) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dismissAction()
                    showDialog.value = false
                }) {
                    Text(dismissText)
                }
            },
            onDismissRequest = { showDialog.value = false }
        )
    } else {
        LaunchedEffect(Unit) { onDismiss() }
    }

    backHandler()

}

@Composable
fun MaterialDialog(
    showDialog: MutableState<Boolean> = mutableStateOf(false),
    icon: ImageVector,
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes confirmText: Int,
    confirmAction: () -> Unit = {},
    @StringRes dismissText: Int,
    dismissAction: () -> Unit = {},
    onDismiss: () -> Unit = {},
    backHandler: @Composable () -> Unit = { BackHandler { showDialog.value = true } }
) {

    if (showDialog.value) {
        AlertDialog(
            icon = { Icon(icon, null) },
            title = { Text(stringResource(title)) },
            text = { Text(stringResource(message), textAlign = TextAlign.Center) },
            confirmButton = {
                TextButton(onClick = {
                    confirmAction()
                    showDialog.value = false
                }) {
                    Text(stringResource(confirmText))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dismissAction()
                    showDialog.value = false
                }) {
                    Text(stringResource(dismissText))
                }
            },
            onDismissRequest = { showDialog.value = false }
        )
    } else {
        LaunchedEffect(Unit) { onDismiss() }
    }

    backHandler()
}