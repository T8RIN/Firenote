package ru.tech.firenote

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)

//        val uid = FirebaseAuth.getInstance().currentUser?.uid
//        val path = "users/" + uid + "/notes"
//        val key = if (note.id.equals("")) mDb.child(path).push().key else note.id
//        val childUpdates: MutableMap<String, Any> = HashMap()
//
//        childUpdates[path + "/" + key] = note.toMap()
//        FirebaseDatabase.getInstance().reference.updateChildren(childUpdates)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isScaffoldVisible by derivedStateOf {
                !mainViewModel.showCreationComposable.currentState || !mainViewModel.showCreationComposable.targetState
            }

            val isAuth = remember {
                mutableStateOf(Firebase.auth.currentUser == null)
            }

            val calcOrientation by derivedStateOf {
                if (mainViewModel.showCreationComposable.targetState || isAuth.value) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }

            val navController = rememberNavController()

            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()

                    requestedOrientation = calcOrientation

                    if (isAuth.value) AuthScreen(isAuth)
                    else {
                        if (isScaffoldVisible) {
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
                                        mainViewModel.showCreationComposable.targetState = true
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
                                        items = listOf(
                                            Screen.NoteListScreen,
                                            Screen.AlarmListScreen
                                        )
                                    )
                                },
                                snackbarHost = { SnackbarHost(snackbarHostState) },
                                modifier = Modifier.nestedScroll(mainViewModel.scrollBehavior.value.nestedScrollConnection)
                            ) {
                                Navigation(navController, dataStore, mainViewModel.viewType)
                            }
                        }

                        mainViewModel.creationComposable()

                        MaterialDialog(
                            showDialog = mutableStateOf(false),
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

        //startService()

    }


//    private fun startService(func: (() -> Unit)? = null) {
//        ForegroundService.func = func
//        val serviceIntent = Intent(this, ForegroundService::class.java)
//        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
//        ContextCompat.startForegroundService(this, serviceIntent)
//    }

}