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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.twotone.FactCheck
import androidx.compose.material.icons.twotone.StickyNote2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import ru.tech.firenote.ui.composable.navigation.Navigation
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.provider.LocalWindowSize
import ru.tech.firenote.ui.composable.screen.auth.AuthScreen
import ru.tech.firenote.ui.composable.screen.creation.GoalCreationScreen
import ru.tech.firenote.ui.composable.screen.creation.NoteCreationScreen
import ru.tech.firenote.ui.composable.single.MaterialDialog
import ru.tech.firenote.ui.composable.single.bar.*
import ru.tech.firenote.ui.composable.utils.WindowSize
import ru.tech.firenote.ui.composable.utils.rememberWindowSizeClass
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.ui.theme.FirenoteTheme
import ru.tech.firenote.viewModel.MainViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isScaffoldVisible by derivedStateOf {
                !mainViewModel.showNoteCreation.currentState
                        || !mainViewModel.showNoteCreation.targetState
                        || !mainViewModel.showGoalCreation.currentState
                        || !mainViewModel.showGoalCreation.targetState
            }
            val navController = rememberNavController()
            val context = LocalContext.current

            val windowSize = rememberWindowSizeClass()
            val splitScreen = windowSize != WindowSize.Compact

            FirenoteTheme {
                MaterialDialog(
                    showDialog = rememberSaveable { mutableStateOf(false) },
                    icon = Icons.Filled.ExitToApp,
                    title = R.string.exitApp,
                    message = R.string.exitAppMessage,
                    confirmText = R.string.stay,
                    dismissText = R.string.close,
                    dismissAction = { finishAffinity() }
                )
                if (mainViewModel.searchMode.value) BackHandler {
                    mainViewModel.searchMode.value = false
                    mainViewModel.searchString.value = ""
                }

                ProvideWindowInsets {
                    val snackbarHostState = remember { SnackbarHostState() }
                    CompositionLocalProvider(
                        LocalSnackbarHost provides snackbarHostState,
                        LocalWindowSize provides windowSize
                    ) {
                        if (mainViewModel.isAuth.value) {
                            AuthScreen(mainViewModel.isAuth)
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        }
                        else {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            if (splitScreen) {
                                Row {
                                    Scaffold(
                                        topBar = {
                                            AppBarWithInsets(
                                                type = APP_BAR_CENTER,
                                                navigationIcon = {
                                                    when (mainViewModel.selectedItem.value) {
                                                        0 -> {
                                                            if (!mainViewModel.searchMode.value) {
                                                                IconButton(onClick = {
                                                                    mainViewModel.searchMode
                                                                        .value = true
                                                                    mainViewModel.searchString.value =
                                                                        ""
                                                                }) {
                                                                    Icon(Icons.Rounded.Search, null)
                                                                }
                                                            } else {
                                                                IconButton(onClick = {
                                                                    mainViewModel.searchMode
                                                                        .value = false
                                                                    mainViewModel.searchString.value =
                                                                        ""
                                                                }) {
                                                                    Icon(
                                                                        Icons.Rounded.ArrowBack,
                                                                        null
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        1 -> {
                                                            if (!mainViewModel.searchMode.value) {
                                                                IconButton(onClick = {
                                                                    mainViewModel.searchMode
                                                                        .value = true
                                                                    mainViewModel.searchString.value =
                                                                        ""

                                                                }) {
                                                                    Icon(Icons.Rounded.Search, null)
                                                                }
                                                            } else {
                                                                IconButton(onClick = {
                                                                    mainViewModel.searchMode
                                                                        .value = false
                                                                    mainViewModel.searchString.value =
                                                                        ""
                                                                }) {
                                                                    Icon(
                                                                        Icons.Rounded.ArrowBack,
                                                                        null
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        2 -> {
                                                            IconButton(onClick = {
                                                                mainViewModel.showUsernameDialog.value =
                                                                    true
                                                            }) {
                                                                Icon(Icons.Outlined.Edit, null)
                                                            }
                                                        }
                                                    }
                                                },
                                                scrollBehavior = mainViewModel.scrollBehavior.value,
                                                title = {
                                                    if (!mainViewModel.searchMode.value) {
                                                        Text(
                                                            text = if (mainViewModel.selectedItem.value == 2) mainViewModel.profileTitle.value
                                                            else stringResource(mainViewModel.title.value),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    } else {
                                                        val localFocusManager =
                                                            LocalFocusManager.current
                                                        BasicTextField(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            value = mainViewModel.searchString.value,
                                                            textStyle = TextStyle(
                                                                fontSize = 22.sp,
                                                                color = MaterialTheme.colorScheme.onBackground,
                                                                textAlign = TextAlign.Start,
                                                            ),
                                                            keyboardActions = KeyboardActions(
                                                                onDone = { localFocusManager.clearFocus() }
                                                            ),
                                                            singleLine = true,
                                                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                                                            onValueChange = {
                                                                mainViewModel.searchString.value =
                                                                    it
                                                            })
                                                        if (mainViewModel.searchString.value.isEmpty()) {
                                                            Text(
                                                                text = stringResource(R.string.searchHere),
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(start = 12.dp),
                                                                style = TextStyle(
                                                                    fontSize = 22.sp,
                                                                    color = MaterialTheme.colorScheme.outline,
                                                                    textAlign = TextAlign.Start,
                                                                )
                                                            )
                                                        }
                                                    }
                                                },
                                                actions = {
                                                    if (!mainViewModel.searchMode.value) {
                                                        when (mainViewModel.selectedItem.value) {
                                                            0 -> NoteActions(mainViewModel)
                                                            1 -> GoalActions(mainViewModel)
                                                            2 -> ProfileActions {
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
                                                    } else {
                                                        IconButton(onClick = {
                                                            mainViewModel.searchString.value = ""
                                                        }) {
                                                            Icon(Icons.Rounded.Close, null)
                                                        }
                                                    }
                                                }
                                            )
                                        },
                                        floatingActionButton = {
                                            ExtendedFloatingActionButton(onClick = {
                                                when (mainViewModel.selectedItem.value) {
                                                    0 -> {
                                                        mainViewModel.showNoteCreation.targetState =
                                                            true
                                                        mainViewModel.globalNote.value = null

                                                        mainViewModel.showGoalCreation.targetState =
                                                            false
                                                        mainViewModel.globalGoal.value = null
                                                    }
                                                    1 -> {
                                                        mainViewModel.showGoalCreation.targetState =
                                                            true
                                                        mainViewModel.globalGoal.value = null

                                                        mainViewModel.showNoteCreation.targetState =
                                                            false
                                                        mainViewModel.globalNote.value = null
                                                    }
                                                    2 -> mainViewModel.resultLauncher.value?.launch(
                                                        "image/*"
                                                    )
                                                }
                                            }, icon = {
                                                when (mainViewModel.selectedItem.value) {
                                                    0 -> Icon(Icons.Outlined.Edit, null)
                                                    1 -> Icon(Icons.Outlined.AddTask, null)
                                                    2 -> Icon(Icons.Outlined.Image, null)
                                                }
                                            }, text = {
                                                when (mainViewModel.selectedItem.value) {
                                                    0 -> Text(stringResource(R.string.addNote))
                                                    1 -> Text(stringResource(R.string.makeGoal))
                                                    2 -> Text(stringResource(R.string.pickImage))
                                                }
                                            })
                                        },
                                        bottomBar = {
                                            BottomNavigationBar(
                                                title = mainViewModel.title,
                                                selectedItem = mainViewModel.selectedItem,
                                                searchMode = mainViewModel.searchMode,
                                                searchString = mainViewModel.searchString,
                                                navController = navController,
                                                items = listOf(
                                                    Screen.NoteListScreen,
                                                    Screen.GoalsScreen,
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
                            }
                            else {
                                Scaffold(
                                    topBar = {
                                        AppBarWithInsets(
                                            type = APP_BAR_CENTER,
                                            navigationIcon = {
                                                when (mainViewModel.selectedItem.value) {
                                                    0 -> {
                                                        if (!mainViewModel.searchMode.value) {
                                                            IconButton(onClick = {
                                                                mainViewModel.searchMode
                                                                    .value = true
                                                                mainViewModel.searchString.value =
                                                                    ""

                                                            }) {
                                                                Icon(Icons.Rounded.Search, null)
                                                            }
                                                        } else {
                                                            IconButton(onClick = {
                                                                mainViewModel.searchMode
                                                                    .value = false
                                                                mainViewModel.searchString.value =
                                                                    ""
                                                            }) {
                                                                Icon(Icons.Rounded.ArrowBack, null)
                                                            }
                                                        }
                                                    }
                                                    1 -> {
                                                        if (!mainViewModel.searchMode.value) {
                                                            IconButton(onClick = {
                                                                mainViewModel.searchMode
                                                                    .value = true
                                                                mainViewModel.searchString.value =
                                                                    ""

                                                            }) {
                                                                Icon(Icons.Rounded.Search, null)
                                                            }
                                                        } else {
                                                            IconButton(onClick = {
                                                                mainViewModel.searchMode
                                                                    .value = false
                                                                mainViewModel.searchString.value =
                                                                    ""
                                                            }) {
                                                                Icon(Icons.Rounded.ArrowBack, null)
                                                            }
                                                        }
                                                    }
                                                    2 -> {
                                                        IconButton(onClick = {
                                                            mainViewModel.showUsernameDialog.value =
                                                                true
                                                        }) {
                                                            Icon(Icons.Outlined.Edit, null)
                                                        }
                                                    }
                                                }
                                            },
                                            scrollBehavior = mainViewModel.scrollBehavior.value,
                                            title = {
                                                if (!mainViewModel.searchMode.value) {
                                                    Text(
                                                        text = if (mainViewModel.selectedItem.value == 2) mainViewModel.profileTitle.value
                                                        else stringResource(mainViewModel.title.value),
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                } else {
                                                    val localFocusManager =
                                                        LocalFocusManager.current
                                                    BasicTextField(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        value = mainViewModel.searchString.value,
                                                        textStyle = TextStyle(
                                                            fontSize = 22.sp,
                                                            color = MaterialTheme.colorScheme.onBackground,
                                                            textAlign = TextAlign.Start,
                                                        ),
                                                        keyboardActions = KeyboardActions(
                                                            onDone = { localFocusManager.clearFocus() }
                                                        ),
                                                        singleLine = true,
                                                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                                                        onValueChange = {
                                                            mainViewModel.searchString.value = it
                                                        })
                                                    if (mainViewModel.searchString.value.isEmpty()) {
                                                        Text(
                                                            text = stringResource(R.string.searchHere),
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(start = 12.dp),
                                                            style = TextStyle(
                                                                fontSize = 22.sp,
                                                                color = MaterialTheme.colorScheme.outline,
                                                                textAlign = TextAlign.Start,
                                                            )
                                                        )
                                                    }
                                                }
                                            },
                                            actions = {
                                                if (!mainViewModel.searchMode.value) {
                                                    when (mainViewModel.selectedItem.value) {
                                                        0 -> NoteActions(mainViewModel)
                                                        1 -> GoalActions(mainViewModel)
                                                        2 -> ProfileActions {
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
                                                } else {
                                                    IconButton(onClick = {
                                                        mainViewModel.searchString.value = ""
                                                    }) {
                                                        Icon(Icons.Rounded.Close, null)
                                                    }
                                                }
                                            }
                                        )
                                    },
                                    floatingActionButton = {
                                        ExtendedFloatingActionButton(onClick = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> {
                                                    mainViewModel.showNoteCreation.targetState =
                                                        true
                                                    mainViewModel.globalNote.value = null
                                                }
                                                1 -> {
                                                    mainViewModel.showGoalCreation.targetState =
                                                        true
                                                    mainViewModel.globalGoal.value = null
                                                }
                                                2 -> mainViewModel.resultLauncher.value?.launch("image/*")
                                            }
                                        }, icon = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> Icon(Icons.Outlined.Edit, null)
                                                1 -> Icon(Icons.Outlined.AddTask, null)
                                                2 -> Icon(Icons.Outlined.Image, null)
                                            }
                                        }, text = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> Text(stringResource(R.string.addNote))
                                                1 -> Text(stringResource(R.string.makeGoal))
                                                2 -> Text(stringResource(R.string.pickImage))
                                            }
                                        })
                                    },
                                    bottomBar = {
                                        BottomNavigationBar(
                                            title = mainViewModel.title,
                                            selectedItem = mainViewModel.selectedItem,
                                            searchMode = mainViewModel.searchMode,
                                            searchString = mainViewModel.searchString,
                                            navController = navController,
                                            items = listOf(
                                                Screen.NoteListScreen,
                                                Screen.GoalsScreen,
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
            if (splitScreen && mainViewModel.selectedItem.value == 0) {
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
        if (!mainViewModel.showGoalCreation.currentState) {
            mainViewModel.globalGoal.value = null
            if (splitScreen && mainViewModel.selectedItem.value in 1..2) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.TwoTone.FactCheck, null, modifier = Modifier.fillMaxSize(0.3f))
                    Text(stringResource(R.string.selectGoal))
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

        AnimatedVisibility(
            visibleState = mainViewModel.showGoalCreation,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { mainViewModel.showGoalCreation.targetState = false }

            GoalCreationScreen(
                state = mainViewModel.showGoalCreation,
                globalGoal = mainViewModel.globalGoal
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