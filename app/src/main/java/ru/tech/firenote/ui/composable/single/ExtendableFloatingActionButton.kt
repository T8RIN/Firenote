package ru.tech.firenote.ui.composable.single

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExtendableFloatingActionButton(
    modifier: Modifier = Modifier,
    extended: Boolean,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {

    FloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp
            ), verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            AnimatedVisibility(visible = extended) {
                Row {
                    Spacer(Modifier.width(12.dp))
                    text()
                }
            }
        }
    }

}