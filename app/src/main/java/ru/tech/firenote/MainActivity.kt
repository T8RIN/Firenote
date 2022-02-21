package ru.tech.firenote

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import ru.tech.firenote.ui.theme.FirenoteTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val mainViewModel: MainViewModel by viewModels()
    //private val viewModel: NoteCreationViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()

            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()

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
                            ExtendedFloatingActionButton(onClick = {
                                mainViewModel.showCreationComposable.value = true
                            }, icon = {
                                mainViewModel.fabIcon()
                            }, text = {
                                mainViewModel.fabText()
                            })
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                title = mainViewModel.title,
                                selectedItem = mainViewModel.selectedItem,
                                navController = navController,
                                items = listOf(Screen.NoteListScreen, Screen.AlarmListScreen)
                            )
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        modifier = Modifier.nestedScroll(mainViewModel.scrollBehavior.value.nestedScrollConnection)
                    ) { contentPadding ->
                        Navigation(navController, dataStore, mainViewModel.viewIcon, contentPadding)
                    }

                    if (mainViewModel.showExitDialog.value) {
                        ExitDialog(
                            icon = Icons.Filled.ExitToApp,
                            title = stringResource(R.string.exitApp),
                            message = stringResource(R.string.exitAppMessage),
                            confirmButton = {
                                TextButton(onClick = { mainViewModel.showExitDialog.value = false }) {
                                    Text("Stay")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    mainViewModel.showExitDialog.value = false
                                    finishAffinity()
                                }) {
                                    Text("Close")
                                }
                            },
                            onDismiss = { mainViewModel.showExitDialog.value = false })
                    }

                    if (mainViewModel.showCreationComposable.value) {
                        mainViewModel.creationComposable()
                    }
                }
            }
            BackHandler(true) {
                mainViewModel.showExitDialog.value = true
            }
        }

        //startService()

    }


    private fun startService(func: (() -> Unit)? = null) {
        ForegroundService.func = func
        val serviceIntent = Intent(this, ForegroundService::class.java)
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
        ContextCompat.startForegroundService(this, serviceIntent)
    }

}