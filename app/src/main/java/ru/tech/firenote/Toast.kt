package ru.tech.firenote

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun Toast(message: String, length: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    SideEffect { Toast.makeText(context, message, length).show() }
}

@Composable
fun Toast(@StringRes message: Int, length: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    SideEffect { Toast.makeText(context, message, length).show() }
}