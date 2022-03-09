package ru.tech.firenote

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import ru.tech.firenote.model.Screen
import ru.tech.firenote.ui.composable.navigation.Navigation
import ru.tech.firenote.ui.composable.screen.AlarmCreationScreen
import ru.tech.firenote.ui.composable.screen.NoteCreationScreen
import ru.tech.firenote.ui.composable.screen.auth.AuthScreen
import ru.tech.firenote.ui.composable.single.*
import ru.tech.firenote.ui.theme.FirenoteTheme
import ru.tech.firenote.viewModel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isScaffoldVisible by derivedStateOf {
                !mainViewModel.showCreationComposable.currentState || !mainViewModel.showCreationComposable.targetState
            }
            val navController = rememberNavController()
            val context = LocalContext.current

            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    CompositionLocalProvider(LocalSnackbarHost provides snackbarHostState) {

                        if (mainViewModel.isAuth.value) AuthScreen(mainViewModel.isAuth)
                        else {
                            Scaffold(
                                topBar = {
                                    AppBarWithInsets(
                                        scrollBehavior = mainViewModel.scrollBehavior.value,
                                        title = stringResource(mainViewModel.title.value),
                                        actions = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> NoteActions(mainViewModel)
                                                1 -> AlarmActions()
                                                2 -> ProfileActions {
                                                    mainViewModel.signOut()
                                                    makeText(
                                                        context,
                                                        R.string.seeYouAgain,
                                                        LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    )
                                },
                                floatingActionButton = {
                                    if (mainViewModel.selectedItem.value != 2) {
                                        ExtendedFloatingActionButton(onClick = {
                                            mainViewModel.showCreationComposable.targetState = true
                                        }, icon = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> Icon(Icons.Outlined.Edit, null)
                                                1 -> Icon(Icons.Outlined.NotificationAdd, null)
                                            }
                                        }, text = {
                                            when (mainViewModel.selectedItem.value) {
                                                0 -> Text(stringResource(R.string.addNote))
                                                1 -> Text(stringResource(R.string.setAlarm))
                                            }
                                        })
                                    }
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

                            Creation(mainViewModel)

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
fun Creation(mainViewModel: MainViewModel) {
    if (!mainViewModel.showCreationComposable.currentState) {
        mainViewModel.globalNote.value = null
    }
    AnimatedVisibility(
        visibleState = mainViewModel.showCreationComposable,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BackHandler { mainViewModel.showCreationComposable.targetState = false }
        when (mainViewModel.selectedItem.value) {
            0 -> NoteCreationScreen(
                state = mainViewModel.showCreationComposable,
                globalNote = mainViewModel.globalNote
            )
            1 -> AlarmCreationScreen()
        }
    }
}