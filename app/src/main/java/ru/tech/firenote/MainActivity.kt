package ru.tech.firenote

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.doOnLayout
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import ru.tech.firenote.ui.theme.FirenoteTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Firenote)
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1")
            .requestEmail()
            .build()

        auth = Firebase.auth
        var currentUser = auth.currentUser

        Toast.makeText(this, currentUser.toString(), Toast.LENGTH_SHORT).show()

        if (currentUser == null) {
//            auth.createUserWithEmailAndPassword("oolbp@bk.com", "password")
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        Log.d("ddd", "createUserWithEmail:success")
//                        currentUser = auth.currentUser
//                        Toast.makeText(this, currentUser.toString(), Toast.LENGTH_SHORT).show()
//                    } else {
//                        Log.w("ddd", "createUserWithEmail:failure", task.exception)
//                        Toast.makeText(
//                            baseContext, "Authentication failed.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
        }

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

            val calcOrientation by derivedStateOf {
                if (mainViewModel.showCreationComposable.targetState) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }

            val navController = rememberNavController()

            FirenoteTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()

                    if (isScaffoldVisible){
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
                                    items = listOf(Screen.NoteListScreen, Screen.AlarmListScreen)
                                )
                            },
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            modifier = Modifier.nestedScroll(mainViewModel.scrollBehavior.value.nestedScrollConnection)
                        ) {
                            Navigation(navController, dataStore, mainViewModel.viewType)
                        }
                    }

                    MaterialDialog(
                        showDialog = mutableStateOf(false),
                        icon = Icons.Filled.ExitToApp,
                        title = R.string.exitApp,
                        message = R.string.exitAppMessage,
                        confirmText = R.string.stay,
                        dismissText = R.string.close,
                        dismissAction = { finishAffinity() }
                    )

                    mainViewModel.creationComposable()
                    requestedOrientation = calcOrientation

                    AuthScreen()
                }
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

@RequiresApi(Build.VERSION_CODES.R)
fun View.addKeyboardListener(keyboardCallback: (visible: Boolean) -> Unit) {
    doOnLayout {
        var keyboardVisible = rootWindowInsets?.isVisible(ime()) == true

        keyboardCallback(keyboardVisible)

        viewTreeObserver.addOnGlobalLayoutListener {
            val keyboardUpdateCheck = rootWindowInsets?.isVisible(ime()) == true
            if (keyboardUpdateCheck != keyboardVisible) {
                keyboardCallback(keyboardUpdateCheck)
                keyboardVisible = keyboardUpdateCheck
            }
        }
    }
}