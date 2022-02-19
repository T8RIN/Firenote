package ru.tech.firenote

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.coroutines.launch
import ru.tech.firenote.ui.theme.FirenoteTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    lateinit var showDialog: MutableState<Boolean>

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            showDialog = rememberSaveable { mutableStateOf(false) }

            val navController = rememberNavController()
            val scrollBehavior by remember { mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior()) }
            val title = rememberSaveable { mutableStateOf(Screen.NoteListScreen.resourceId) }
            val selectedItem = rememberSaveable { mutableStateOf(0) }
            val showViewMenu = rememberSaveable { mutableStateOf(false) }
            val showFilter = rememberSaveable { mutableStateOf(false) }

            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()

                    Scaffold(
                        topBar = {
                            AppBarWithInsets(
                                scrollBehavior = scrollBehavior,
                                title = stringResource(title.value),
                                actions = {
                                    when (selectedItem.value) {
                                        0 -> NoteActions(showViewMenu = showViewMenu, showFilter)
                                        1 -> AlarmActions(showFilter = showFilter)
                                    }
                                }
                            )
                        },
                        floatingActionButton = {
                            ExtendedFloatingActionButton(onClick = { /* TODO: doSomething() */
                                scope.launch {
                                    val snackbarResult = snackbarHostState.showSnackbar(
                                        message = "Test" /* TODO: message */,
                                        actionLabel = "Undo" /* TODO: actionText */
                                    )
                                    if (snackbarResult == SnackbarResult.ActionPerformed)
                                        showDialog.value = true // TODO: Action
                                }
                            }, icon = {
                                when (selectedItem.value) {
                                    0 -> Icon(Icons.Outlined.Edit, contentDescription = null)
                                    1 -> Icon(
                                        Icons.Outlined.NotificationAdd,
                                        contentDescription = null
                                    )
                                }
                            }, text = {
                                when (selectedItem.value) {
                                    0 -> Text(stringResource(R.string.addNote))
                                    1 -> Text(stringResource(R.string.setAlarm))
                                }
                            })
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                title = title,
                                selectedItem = selectedItem,
                                navController = navController,
                                items = listOf(Screen.NoteListScreen, Screen.AlarmListScreen)
                            )
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    ) { contentPadding ->
                        Navigation(navController, contentPadding)
                    }

                    if (showDialog.value) {
                        ComposeDialog(
                            icon = Icons.Filled.ExitToApp,
                            title = stringResource(R.string.exitApp),
                            message = stringResource(R.string.exitAppMessage),
                            confirmButton = {
                                TextButton(onClick = { showDialog.value = false }) {
                                    Text("Stay")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showDialog.value = false
                                    finishAffinity()
                                }) {
                                    Text("Close")
                                }
                            },
                            onDismiss = { showDialog.value = false })
                    }
                }
            }
        }

        //startService()

    }

    override fun onBackPressed() {
        showDialog.value = true
    }

    private fun startService(func: (() -> Unit)? = null) {
        ForegroundService.func = func
        val serviceIntent = Intent(this, ForegroundService::class.java)
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
        ContextCompat.startForegroundService(this, serviceIntent)
    }

}