package ru.tech.firenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.tech.firenote.ui.composable.app.FirenoteApp
import ru.tech.firenote.ui.composable.utils.WindowSize
import ru.tech.firenote.ui.composable.utils.rememberWindowSizeClass


@ExperimentalFoundationApi
@AndroidEntryPoint
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()
            val windowSize = rememberWindowSizeClass()
            val splitScreen = windowSize != WindowSize.Compact

            FirenoteApp(
                context = this,
                windowSize = windowSize,
                splitScreen = splitScreen,
                navController = navController
            )
        }
    }
}