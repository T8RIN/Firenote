package ru.tech.firenote.ui.composable.single.bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.accompanist.insets.statusBarsPadding
import ru.tech.firenote.ui.composable.single.text.EditText

@Composable
fun EditableAppBar(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    hint: String,
    color: Color,
    backgroundColor: Color,
    text: MutableState<String> = mutableStateOf(""),
    errorColor: Int = MaterialTheme.colorScheme.error.toArgb(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {}
) {
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    Surface(color = Color.DarkGray) {
        Surface(color = backgroundColor) {
            SmallTopAppBar(
                title = {
                    EditText(
                        modifier = textModifier.fillMaxWidth(),
                        hintText = hint,
                        onValueChange = onValueChange,
                        errorColor = errorColor,
                        color = color,
                        enabled = enabled,
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

}