package ru.tech.firenote

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

    lateinit var showDialog: MutableState<Boolean>

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            showDialog = remember { mutableStateOf(false) }
            val navController = rememberNavController()
            val scrollBehavior by remember { mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior()) }
            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Scaffold(
                        topBar = {
                            AppBarWithInsets(
                                scrollBehavior = scrollBehavior,
                                title = "FireNote",
                                actions = {
                                    IconButton(onClick = { /* TODO: doSomething() */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.FilterAlt,
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(onClick = { /* TODO: doSomething() */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.ViewCompact,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }, bottomBar = {
                            BottomNavigationBar(
                                navController = navController,
                                items = listOf(Screen.NoteListScreen, Screen.CalendarScreen)
                            )
                        },
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

@Composable
fun CustomIcon(icon: ImageVector) {
    Icon(icon, contentDescription = null, modifier = Modifier.size(128.dp))
}