package ru.tech.firenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
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

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            val scrollBehavior by remember { mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior()) }
            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Scaffold(
                        topBar = {
                            AppBarWithInsets(
                                scrollBehavior = scrollBehavior,
                                title = "Centered TopAppBar",
                                actions = {
                                    IconButton(onClick = { /* doSomething() */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
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
                }
            }
        }

//        val work: WorkRequest = OneTimeWorkRequestBuilder<CustomWorker>()
//            .setInitialDelay(10, TimeUnit.SECONDS)
//            .build()
//
//        WorkManager
//            .getInstance(this)
//            .enqueue(work)

        //startService()

    }

//    private fun startService(func: (() -> Unit)? = null) {
//        ForegroundService.func = func
//        val serviceIntent = Intent(this, ForegroundService::class.java)
//        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
//        ContextCompat.startForegroundService(this, serviceIntent)
//    }

}

@Composable
fun CustomIcon(icon: ImageVector) {
    Icon(icon, contentDescription = null, modifier = Modifier.size(128.dp))
}