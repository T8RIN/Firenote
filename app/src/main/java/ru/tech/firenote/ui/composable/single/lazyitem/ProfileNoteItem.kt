package ru.tech.firenote.ui.composable.single.lazyitem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tech.firenote.R
import ru.tech.firenote.utils.GlobalUtils.blend

@Composable
fun ProfileNoteItem(
    pair: Pair<Color, Int>,
    typeText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp
) {
    val localFocusManager = LocalFocusManager.current
    Box(
        modifier = modifier,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = pair.first,
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(pair.first.toArgb().blend()),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp, bottom = 40.dp, end = 40.dp, start = 40.dp)
        )
        Text(
            text = pair.second.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 10.dp, top = 10.dp, end = cutCornerSize)
        )
        var txt by remember { mutableStateOf(typeText) }
        BasicTextField(
            value = txt,
            onValueChange = {
                txt = it
                onValueChange(it)
            },
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 11.sp
            ),
            keyboardActions = KeyboardActions(
                onDone = { localFocusManager.clearFocus() }
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(end = 5.dp, start = 5.dp, bottom = 5.dp),
            maxLines = 3
        )

        if (txt.isEmpty()) {
            Text(
                text = stringResource(R.string.noteType),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(end = 5.dp, start = 5.dp, bottom = 5.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 11.sp
                )
            )
        }
    }
}