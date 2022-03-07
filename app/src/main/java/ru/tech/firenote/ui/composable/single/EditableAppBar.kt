package ru.tech.firenote.ui.composable.single

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun EditableAppBar(
    modifier: Modifier = Modifier,
    hint: String,
    color: Color,
    text: MutableState<String> = mutableStateOf(""),
    errorColor: Int = MaterialTheme.colorScheme.error.toArgb(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {
    val backgroundColors = TopAppBarDefaults.smallTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    ).value
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    Surface(color = backgroundColor) {
        SmallTopAppBar(
            title = {
                EditText(
                    hintText = hint,
                    onValueChange = onValueChange,
                    errorColor = errorColor,
                    color = color,
                    textFieldState = text
                )
            },
            navigationIcon = navigationIcon,
            scrollBehavior = scrollBehavior,
            colors = foregroundColors,
            modifier = modifier.statusBarsPadding(),
            actions = actions
        )
    }
}