package ru.tech.firenote.ui.composable.provider

import androidx.compose.runtime.compositionLocalOf
import ru.tech.firenote.ui.composable.single.toast.FancyToastValues

val LocalToastHost = compositionLocalOf { FancyToastValues() }