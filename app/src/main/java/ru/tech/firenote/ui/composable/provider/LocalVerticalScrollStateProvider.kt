package ru.tech.firenote.ui.composable.provider

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.compositionLocalOf

val LocalLazyListStateProvider = compositionLocalOf { LazyListState() }