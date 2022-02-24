package ru.tech.firenote

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStore: DataStore<Preferences>
) : ViewModel() {

    val title = mutableStateOf(Screen.NoteListScreen.resourceId)

    val selectedItem = mutableStateOf(0)
    val showCreationComposable = MutableTransitionState(false).apply {
        targetState = false
    }


    @OptIn(ExperimentalMaterial3Api::class)
    val scrollBehavior = mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior())

    private val showViewMenu = mutableStateOf(false)
    private val showFilter = mutableStateOf(false)
    val viewIcon = mutableStateOf(
        runBlocking {
            dataStore.data.map {
                it[PreferenceKeys.VIEW_COMPOSITION] ?: 0
            }.first()
        }
    )

    val fabIcon: @Composable () -> Unit = {
        when (selectedItem.value) {
            0 -> Icon(Icons.Outlined.Edit, null)
            1 -> Icon(Icons.Outlined.NotificationAdd, null)
        }
    }

    val fabText: @Composable () -> Unit = {
        when (selectedItem.value) {
            0 -> Text(stringResource(R.string.addNote))
            1 -> Text(stringResource(R.string.setAlarm))
        }
    }

    val mainAppBarActions: @Composable () -> Unit = {
        when (selectedItem.value) {
            0 -> NoteActions(
                viewIcon,
                showViewMenu,
                showFilter,
                dataStore
            )
            1 -> AlarmActions(showFilter = showFilter)
        }
    }

    val creationComposable: @Composable () -> Unit = {
        AnimatedVisibility(
            visibleState = showCreationComposable,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { showCreationComposable.targetState = false }
            when (selectedItem.value) {
                0 -> NoteCreationScreen(state = showCreationComposable)
                1 -> AlarmCreationScreen()
            }
        }
    }

    fun showSnackBar(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        scope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = "Test" /* TODO: message */,
                actionLabel = "Undo" /* TODO: actionText */
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                // TODO: Action
            }
        }
    }

}