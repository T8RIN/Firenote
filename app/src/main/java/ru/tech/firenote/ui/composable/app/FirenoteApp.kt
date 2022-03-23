package ru.tech.firenote.ui.composable.app

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.provider.LocalWindowSize
import ru.tech.firenote.ui.composable.screen.auth.AuthScreen
import ru.tech.firenote.ui.composable.screen.creation.CreationContainer
import ru.tech.firenote.ui.composable.single.dialog.MaterialDialog
import ru.tech.firenote.ui.composable.single.scaffold.FirenoteScaffold
import ru.tech.firenote.ui.composable.utils.WindowSize
import ru.tech.firenote.ui.theme.FirenoteTheme
import ru.tech.firenote.viewModel.main.MainViewModel

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirenoteApp(
    context: ComponentActivity,
    windowSize: WindowSize,
    splitScreen: Boolean,
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {

    val isScaffoldVisible by derivedStateOf {
        (!viewModel.showNoteCreation.currentState or !viewModel.showNoteCreation.targetState)
            .and(!viewModel.showGoalCreation.currentState or !viewModel.showGoalCreation.targetState)
    }

    FirenoteTheme {
        MaterialDialog(
            showDialog = rememberSaveable { mutableStateOf(false) },
            icon = Icons.Filled.ExitToApp,
            title = R.string.exitApp,
            message = R.string.exitAppMessage,
            confirmText = R.string.stay,
            dismissText = R.string.close,
            dismissAction = { context.finishAffinity() }
        )
        if (viewModel.searchMode.value) BackHandler {
            viewModel.searchMode.value = false
            viewModel.updateSearch()
        }


        val snackbarHostState = remember { SnackbarHostState() }
        CompositionLocalProvider(
            LocalSnackbarHost provides snackbarHostState,
            LocalWindowSize provides windowSize
        ) {
            if (viewModel.isAuth.value) {
                AuthScreen(viewModel.isAuth)
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                if (splitScreen) {
                    Row {
                        FirenoteScaffold(
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel,
                            navController = navController,
                            context = context
                        )
                        Surface(modifier = Modifier.weight(1.5f)) {
                            CreationContainer(viewModel, splitScreen)
                        }
                    }
                } else {
                    FirenoteScaffold(
                        modifier = Modifier.alpha(if (isScaffoldVisible) 1f else 0f),
                        viewModel = viewModel,
                        navController = navController,
                        context = context
                    )
                    CreationContainer(viewModel, splitScreen)
                }
            }
        }
    }

}