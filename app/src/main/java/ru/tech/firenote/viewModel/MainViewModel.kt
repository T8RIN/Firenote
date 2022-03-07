package ru.tech.firenote.viewModel

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.tech.firenote.R
import ru.tech.firenote.model.Note
import ru.tech.firenote.model.Screen
import ru.tech.firenote.ui.composable.screen.AlarmCreationScreen
import ru.tech.firenote.ui.composable.screen.NoteCreationScreen
import ru.tech.firenote.ui.composable.single.AlarmActions
import ru.tech.firenote.ui.composable.single.NoteActions
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val dataStore: DataStore<Preferences>
) : ViewModel() {

    val title = mutableStateOf(Screen.NoteListScreen.resourceId)

    val selectedItem = mutableStateOf(0)
    val globalNote: MutableState<Note?> = mutableStateOf(null)
    val showCreationComposable = MutableTransitionState(false).apply {
        targetState = false
    }


    @OptIn(ExperimentalMaterial3Api::class)
    val scrollBehavior = mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior())

    val showFilter = mutableStateOf(false)


    val fab: @Composable () -> Unit = {
        if (selectedItem.value != 2) {
            ExtendedFloatingActionButton(onClick = {
                showCreationComposable.targetState = true
            }, icon = {
                when (selectedItem.value) {
                    0 -> Icon(Icons.Outlined.Edit, null)
                    1 -> Icon(Icons.Outlined.NotificationAdd, null)
                }
            }, text = {
                when (selectedItem.value) {
                    0 -> Text(stringResource(R.string.addNote))
                    1 -> Text(stringResource(R.string.setAlarm))
                }
            })
        }
    }

    val mainAppBarActions: @Composable () -> Unit = {
        when (selectedItem.value) {
            0 -> NoteActions(this)
            1 -> AlarmActions(showFilter = showFilter)
        }
    }

    val creationComposable: @Composable () -> Unit = {
        if (!showCreationComposable.currentState) {
            globalNote.value = null
        }
        AnimatedVisibility(
            visibleState = showCreationComposable,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { showCreationComposable.targetState = false }
            when (selectedItem.value) {
                0 -> NoteCreationScreen(state = showCreationComposable, globalNote = globalNote)
                1 -> AlarmCreationScreen()
            }
        }
    }

    fun setFilter(value: Boolean) {
        showFilter.value = value
    }

//    fun showSnackBar(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
//        scope.launch {
//            val snackbarResult = snackbarHostState.showSnackbar(
//                message = "Test" /* TODO: message */,
//                actionLabel = "Undo" /* TODO: actionText */
//            )
//            if (snackbarResult == SnackbarResult.ActionPerformed) {
//                // TODO: Action
//            }
//        }
//    }

}