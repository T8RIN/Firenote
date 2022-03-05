package ru.tech.firenote.ui.composable.single

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Gradient(modifier: Modifier = Modifier, startColor: Color, endColor: Color) {
    Box(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    startColor,
                    endColor
                )
            )
        )
    )
}
