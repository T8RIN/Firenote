package ru.tech.firenote.ui.composable.single.lazyitem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.tech.firenote.model.Goal
import ru.tech.firenote.ui.composable.provider.LocalWindowSize
import ru.tech.firenote.ui.composable.utils.WindowSize
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GoalItem(
    goal: Goal,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    onDeleteClick: () -> Unit
) {
    var doneAll = true
    val mapped = goal.content?.mapIndexed { index, item ->
        var text = ""
        text += ("• ${item.content}")
        if (index != goal.content.lastIndex) text += "\n"
        if (item.done == false) doneAll = false
        item.copy(content = text)
    }

    Box(
        modifier = modifier,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(goal.color ?: 0),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            val convertTime by derivedStateOf {
                SimpleDateFormat("dd/MM/yyyy\nHH:mm", Locale.getDefault()).format(
                    goal.timestamp ?: 0L
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(2f),
                    text = goal.title ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (doneAll) Color.DarkGray else Color.Black,
                    textDecoration = if (doneAll) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = convertTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Justify,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(Modifier.padding(end = 32.dp)) {
                mapped?.let {
                    it.forEachIndexed { index, item ->
                        if (index <= when (LocalWindowSize.current) {
                                WindowSize.Compact -> 10
                                WindowSize.Medium -> 20
                                else -> 30
                            }
                        ) {
                            Text(
                                text = item.content ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.done == true) Color.DarkGray else Color.Black,
                                textDecoration = if (item.done == true) TextDecoration.LineThrough else TextDecoration.None,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 5
                            )
                        }
                    }
                }
            }
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete note",
                tint = darkColorScheme().onTertiary
            )
        }
    }
}