package ru.tech.firenote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MaterialTextField(
    modifier: Modifier = Modifier,
    hintText: String = "",
    textFieldState: MutableState<String> = mutableStateOf(""),
    endPaddingIcon: Dp = 10.dp,
    cursorColor: Color = Color.Black,
    singleLine: Boolean = true,
    color: Color = MaterialTheme.colorScheme.onBackground,
    errorEnabled: Boolean = true,
    topPadding: Dp = 0.dp,
    errorColor: MutableState<Color> = mutableStateOf(MaterialTheme.colorScheme.error),
    onValueChange: (String) -> Unit = {}
) {
    val localFocusManager = LocalFocusManager.current

    Box(Modifier.padding(top = topPadding)) {
        BasicTextField(
            modifier = modifier.fillMaxWidth(),
            value = textFieldState.value,
            onValueChange = {
                onValueChange(it)
                textFieldState.value = it
            },
            cursorBrush = SolidColor(cursorColor),
            textStyle = TextStyle(
                fontSize = 22.sp,
                color = color,
                textAlign = TextAlign.Start,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = { localFocusManager.clearFocus() }
            ),
            singleLine = singleLine
        )

        if (textFieldState.value.isEmpty()) {
            val color =
                if (errorEnabled) errorColor.value
                else MaterialTheme.colorScheme.onSurfaceVariant
            Text(
                hintText,
                Modifier
                    .padding(start = 40.dp)
                    .align(
                        Alignment.TopStart
                    ),
                fontSize = 22.sp,
                color = color
            )
            if (errorEnabled) {
                Icon(
                    Icons.Filled.ErrorOutline, null, tint = color, modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .padding(end = endPaddingIcon)
                )
            }
        }
    }
}