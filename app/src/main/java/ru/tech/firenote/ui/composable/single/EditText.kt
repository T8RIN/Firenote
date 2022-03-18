package ru.tech.firenote.ui.composable.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    hintText: String = "",
    textFieldState: MutableState<String> = mutableStateOf(""),
    endPaddingIcon: Dp = 10.dp,
    cursorColor: Color = Color.Black,
    singleLine: Boolean = true,
    color: Color = MaterialTheme.colorScheme.onBackground,
    errorEnabled: Boolean = true,
    shadowColor: Color = Color.DarkGray,
    topPadding: Dp = 0.dp,
    enabled: Boolean = true,
    errorColor: Int = MaterialTheme.colorScheme.error.toArgb(),
    onValueChange: (String) -> Unit = {}
) {
    val localFocusManager = LocalFocusManager.current

    Box(Modifier.padding(top = topPadding)) {
        BasicTextField(
            modifier = modifier,
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
            singleLine = singleLine,
            enabled = enabled
        )

        if (textFieldState.value.isEmpty()) {
            val localColor =
                if (errorEnabled) Color(errorColor)
                else MaterialTheme.colorScheme.onSurfaceVariant
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    hintText,
                    Modifier
                        .weight(1f)
                        .padding(start = 40.dp),
                    fontSize = 22.sp,
                    color = localColor,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(
                            color = shadowColor,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                if (errorEnabled) {
                    Icon(
                        Icons.Filled.ErrorOutline, null, tint = localColor, modifier = Modifier
                            .padding(end = endPaddingIcon)
                    )
                }
            }
        }
    }
}