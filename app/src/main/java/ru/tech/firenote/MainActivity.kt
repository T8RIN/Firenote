package ru.tech.firenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import ru.tech.firenote.model.Screen
import ru.tech.firenote.ui.composable.navigation.Navigation
import ru.tech.firenote.ui.composable.screen.auth.AuthScreen
import ru.tech.firenote.ui.composable.single.AppBarWithInsets
import ru.tech.firenote.ui.composable.single.BottomNavigationBar
import ru.tech.firenote.ui.composable.single.MaterialDialog
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

            val isAuth = remember {
                mutableStateOf(Firebase.auth.currentUser == null)
            }

            val navController = rememberNavController()

            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    CompositionLocalProvider(LocalSnackbarHost provides snackbarHostState) {

                        if (isAuth.value) AuthScreen(isAuth)
                        else {
                            Scaffold(
                                topBar = {
                                    AppBarWithInsets(
                                        scrollBehavior = mainViewModel.scrollBehavior.value,
                                        title = stringResource(mainViewModel.title.value),
                                        actions = {
                                            mainViewModel.mainAppBarActions()
                                        }
                                    )
                                },
                                floatingActionButton = {
                                    mainViewModel.fab()
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

                            mainViewModel.creationComposable()

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

        //startService()

    }


//    private fun startService(func: (() -> Unit)? = null) {
//        ForegroundService.func = func
//        val serviceIntent = Intent(this, ForegroundService::class.java)
//        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
//        ContextCompat.startForegroundService(this, serviceIntent)
//    }

}