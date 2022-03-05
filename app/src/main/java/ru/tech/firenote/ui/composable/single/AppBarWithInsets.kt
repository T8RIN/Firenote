package ru.tech.firenote.ui.composable.single


import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun AppBarWithInsets(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
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
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = navigationIcon,
            actions = actions,
            scrollBehavior = scrollBehavior,
            colors = foregroundColors,
            modifier = modifier.statusBarsPadding()
        )
    }
}