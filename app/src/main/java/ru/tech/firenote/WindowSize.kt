package ru.tech.firenote

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator


/**
 * Opinionated set of viewport breakpoints
 *     - Compact: Most phones in portrait mode
 *     - Medium: Most foldables and tablets in portrait mode
 *     - Expanded: Most tablets in landscape mode
 *
 */
enum class WindowSize { Compact, Medium, Expanded }

@Composable
fun Activity.rememberWindowSizeClass(): WindowSize {
    val windowSize = rememberWindowSize()
    val windowDpSize = with(LocalDensity.current) {
        windowSize.toDpSize()
    }
    return getWindowSizeClass(windowDpSize)
}

@Composable
private fun Activity.rememberWindowSize(): Size {
    val configuration = LocalConfiguration.current
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    return windowMetrics.bounds.toComposeRect().size
}

fun getWindowSizeClass(windowDpSize: DpSize): WindowSize {
    val width = when {
        windowDpSize.width < 0.dp -> throw IllegalArgumentException("Dp value cannot be negative")
        windowDpSize.width < 600.dp -> WindowSize.Compact
        windowDpSize.width < 840.dp -> WindowSize.Medium
        else -> WindowSize.Expanded
    }
    val height = when {
        windowDpSize.height < 0.dp -> throw IllegalArgumentException("Dp value cannot be negative")
        windowDpSize.height < 480.dp -> WindowSize.Compact
        windowDpSize.height < 900.dp -> WindowSize.Medium
        else -> WindowSize.Expanded
    }
    return width minOf height
}

infix fun WindowSize.minOf(size: WindowSize): WindowSize {
    return if (size > this) this
    else size
}