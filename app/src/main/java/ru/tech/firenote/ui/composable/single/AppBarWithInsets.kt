package ru.tech.firenote.ui.composable.single


import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun AppBarWithInsets(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    type: Int = APP_BAR_SIMPLE,
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
        when (type) {
            APP_BAR_CENTER -> {
                CenterAlignedTopAppBar(
                    title = title,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                    colors = foregroundColors,
                    modifier = modifier.statusBarsPadding()
                )
            }
            APP_BAR_SIMPLE -> {
                SmallTopAppBar(
                    title = title,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                    colors = foregroundColors,
                    modifier = modifier.statusBarsPadding()
                )
            }
        }
    }
}

const val APP_BAR_SIMPLE = 0
const val APP_BAR_CENTER = 1