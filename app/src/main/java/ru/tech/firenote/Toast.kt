package ru.tech.firenote

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun Toast(message: String, length: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { Toast.makeText(context, message, length).show() }
}

@Composable
fun Toast(@StringRes message: Int, length: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { Toast.makeText(context, message, length).show() }
}