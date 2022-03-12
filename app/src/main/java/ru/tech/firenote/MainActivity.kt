package ru.tech.firenote

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.twotone.StickyNote2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import ru.tech.firenote.model.Screen
import ru.tech.firenote.ui.composable.navigation.Navigation
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.provider.LocalWindowSize
import ru.tech.firenote.ui.composable.screen.NoteCreationScreen
import ru.tech.firenote.ui.composable.screen.auth.AuthScreen
import ru.tech.firenote.ui.composable.single.*
import ru.tech.firenote.ui.theme.FirenoteTheme
import ru.tech.firenote.viewModel.MainViewModel
import kotlin.math.min

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)

        val dp = min(resources.configuration.screenHeightDp, resources.configuration.screenWidthDp)
        requestedOrientation = when {
            dp < 600 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isScaffoldVisible by derivedStateOf {
                !mainViewModel.showNoteCreation.currentState || !mainViewModel.showNoteCreation.targetState
            }
            val navController = rememberNavController()
            val context = LocalContext.current

            val windowSize = rememberWindowSizeClass()
            val splitScreen = windowSize != WindowSize.Compact

            FirenoteTheme {
                ProvideWindowInsets {
                    val snackbarHostState = remember { SnackbarHostState() }
                    CompositionLocalProvider(
                        LocalSnackbarHost provides snackbarHostState,
                        LocalWindowSize provides windowSize
                    ) {
                        if (mainViewModel.isAuth.value) {
                            AuthScreen(mainViewModel.isAuth)
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        } else {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            if (splitScreen) {
                                Row {
                                    Scaffold(
                                        topBar = {
                                            when (mainViewModel.selectedItem.value) {
                                                2 -> {
                                                    AppBarWithInsets(
                                                        type = APP_BAR_CENTER,
                                                        navigationIcon = {
                                                            IconButton(onClick = {
                                                                mainViewModel.showUsernameDialog.value =
                                                                    true
                                                            }) {
                                                                Icon(Icons.Rounded.Edit, null)
                                                            }
                                                        },
                                                        scrollBehavior = mainViewModel.scrollBehavior.value,
                                                        title = mainViewModel.profileTitle.value,
                                                        actions = {
                                                            ProfileActions {
                                                                navController.navigate(Screen.NoteListScreen.route) {
                                                                    navController.popBackStack()
                                                                    launchSingleTop = true
                                                                }
                                                                makeText(
                                                                    context,
                                                                    R.string.seeYouAgain,
                                                                    LENGTH_SHORT
                                                                ).show()

                                                                mainViewModel.showNoteCreation.targetState =
                                                                    false
                                                                mainViewModel.globalNote.value =
                                                                    null
                                                                mainViewModel.signOut()
                                                            }
                                                        }
                                                    )
                                                }
                                                else -> {
                                                    AppBarWithInsets(
                                                        scrollBehavior = mainViewModel.scrollBehavior.value,
                                                        title = stringResource(mainViewModel.title.value),
                                                        actions = {
                                                            when (mainViewModel.selectedItem.value) {
                                                                0 -> NoteActions(mainViewModel)
                                                                1 -> AlarmActions()
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        },
                                        floatingActionButton = {
                                            ExtendedFloatingActionButton(onClick = {
                                                when (mainViewModel.selectedItem.value) {
                                                    0 -> {
                                                        mainViewModel.showNoteCreation.targetState =
                                                            true
                                                        mainViewModel.globalNote.value = null
                                                    }
                                                    2 -> mainViewModel.resultLauncher.value?.launch(
                                                        "image/*"
                                                    )
                                                }
                                            }, icon = {
                                                when (mainViewModel.selectedItem.value) {
                                                    0 -> Icon(Icons.Outlined.Edit, null)
                                                    1 -> Icon(Icons.Outlined.NotificationAdd, null)
                                                    2 -> Icon(Icons.Outlined.Image, null)
                                                }
                                            }, text = {
                                                when (mainViewModel.selectedItem.value) {
                                                    0 -> Text(stringResource(R.string.addNote))
                                                    1 -> Text(stringResource(R.string.setAlarm))
                                                    2 -> Text(stringResource(R.string.pickImage))
                                                }
                                            })
                                        },
                                        bottomBar = {
                                            BottomNavigationBar(
                                                title = mainViewModel.title,
                                                selectedItem = mainViewModel.selectedItem,
                                                navController = navController,
                                                items = listOf(
                                                    Screen.NoteListScreen,
                                                    Screen.AlarmListScreen,
                                                    Screen.ProfileScreen
                                                ),
                                                alwaysShowLabel = false
                                            )
                                        },
                                        snackbarHost = { SnackbarHost(LocalSnackbarHost.current) },
                                        modifier = Modifier
                                            .nestedScroll(mainViewModel.scrollBehavior.value.nestedScrollConnection)
                                            .weight(1f)
                                    ) { contentPadding ->
                                        Navigation(navController, contentPadding, mainViewModel)
                                    }
                                    Surface(modifier = Modifier.weight(1.5f)) {
                                        Creation(mainViewModel, splitScreen)
                                    }
                                }
                            } else {
                                Scaffold(
                                    topBar = {
                                        when (mainViewModel.selectedItem.value) {
                                            2 -> {
                                                AppBarWithInsets(
                                                    type = APP_BAR_CENTER,
                                                    navigationIcon = {
                                                        IconButton(onClick = {
                                                            mainViewModel.showUsernameDialog.value =
                                                                true
                                                        }) {
                                                            Icon(Icons.Rounded.Edit, null)
                                                        }
                                                    },
                                                    scrollBehavior = mainViewModel.scrollBehavior.value,
                                                    title = mainViewModel.profileTitle.value,
                                                    actions = {
                                                        ProfileActions {
                                                            navController.navigate(Screen.NoteListScreen.route) {
                                                                navController.popBackStack()
                                                                launchSingleTop = true
                                                            }
                                                            makeText(
                                                                context,
                                                                R.string.seeYouAgain,
                                                                LENGTH_SHORT
                                                            ).show()

                                                            mainViewModel.signOut()
                                                        }
                                                    }
                                                )
                                            }
                                            else -> {
                                                AppBarWithInsets(
                                                    scrollBehavior = mainViewModel.scrollBehavior.value,
                                                    title = stringResource(mainViewModel.title.value),
                                                    actions = {
                                                        when (mainViewModel.selectedItem.value) {
                                                            0 -> NoteActions(mainViewModel)
                                                            1 -> AlarmActions()
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    },
                                    floatingActionButton = {
                                        ExtendedFloatingActionButton(onClick = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> {
                                                    mainViewModel.showNoteCreation.targetState =
                                                        true
                                                    mainViewModel.globalNote.value = null
                                                }
                                                2 -> mainViewModel.resultLauncher.value?.launch("image/*")
                                            }
                                        }, icon = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> Icon(Icons.Outlined.Edit, null)
                                                1 -> Icon(Icons.Outlined.NotificationAdd, null)
                                                2 -> Icon(Icons.Outlined.Image, null)
                                            }
                                        }, text = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> Text(stringResource(R.string.addNote))
                                                1 -> Text(stringResource(R.string.setAlarm))
                                                2 -> Text(stringResource(R.string.pickImage))
                                            }
                                        })
                                    },
                                    bottomBar = {
                                        BottomNavigationBar(
                                            title = mainViewModel.title,
                                            selectedItem = mainViewModel.selectedItem,
                                            navController = navController,
                                            items = listOf(
                                                Screen.NoteListScreen,
                                                Screen.AlarmListScreen,
                                                Screen.ProfileScreen
                                            ),
                                            alwaysShowLabel = false
                                        )
                                    },
                                    snackbarHost = { SnackbarHost(LocalSnackbarHost.current) },
                                    modifier = Modifier
                                        .nestedScroll(mainViewModel.scrollBehavior.value.nestedScrollConnection)
                                        .alpha(if (isScaffoldVisible) 1f else 0f)
                                ) { contentPadding ->
                                    Navigation(navController, contentPadding, mainViewModel)
                                }
                                Creation(mainViewModel, splitScreen)
                            }

                            MaterialDialog(
                                showDialog = rememberSaveable { mutableStateOf(false) },
                                icon = Icons.Filled.ExitToApp,
                                title = R.string.exitApp,
                                message = R.string.exitAppMessage,
                                confirmText = R.string.stay,
                                dismissText = R.string.close,
                                dismissAction = { finishAffinity() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Creation(mainViewModel: MainViewModel, splitScreen: Boolean) {
    Box(Modifier.fillMaxSize()) {
        if (!mainViewModel.showNoteCreation.currentState) {
            mainViewModel.globalNote.value = null
            if (splitScreen) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.TwoTone.StickyNote2, null, modifier = Modifier.fillMaxSize(0.3f))
                    Text(stringResource(R.string.selectNote))
                }
            }
        }
        AnimatedVisibility(
            visibleState = mainViewModel.showNoteCreation,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { mainViewModel.showNoteCreation.targetState = false }

            NoteCreationScreen(
                state = mainViewModel.showNoteCreation,
                globalNote = mainViewModel.globalNote
            )
        }
        if (splitScreen) {
            Divider(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}