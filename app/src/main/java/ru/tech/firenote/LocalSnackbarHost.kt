package ru.tech.firenote

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalSnackbarHost = compositionLocalOf { SnackbarHostState() }

fun showSnackbar(
    scope: CoroutineScope,
    host: SnackbarHostState,
    message: String,
    action: String,
    result: (SnackbarResult) -> Unit
) {
    scope.launch {
        result(
            host.showSnackbar(
                message = message,
                actionLabel = action
            )
        )
    }
}