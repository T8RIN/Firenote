package ru.tech.firenote.ui.composable.provider

import androidx.compose.runtime.compositionLocalOf
import ru.tech.firenote.ui.composable.utils.WindowSize

val LocalWindowSize = compositionLocalOf { WindowSize.Compact }
