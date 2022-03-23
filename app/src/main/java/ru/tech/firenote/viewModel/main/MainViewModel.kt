package ru.tech.firenote.viewModel.main

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.tech.firenote.model.Goal
import ru.tech.firenote.model.Note
import ru.tech.firenote.repository.NoteRepository
import ru.tech.firenote.ui.route.Screen
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val title = mutableStateOf(Screen.NoteListScreen.resourceId)
    val profileTitle = mutableStateOf("")

    val selectedItem = mutableStateOf(0)

    val globalNote: MutableState<Note?> = mutableStateOf(null)
    val showNoteCreation = MutableTransitionState(false).apply {
        targetState = false
    }

    val globalGoal: MutableState<Goal?> = mutableStateOf(null)
    val showGoalCreation = MutableTransitionState(false).apply {
        targetState = false
    }

    val searchMode = mutableStateOf(false)
    val searchString = mutableStateOf("")

    val resultLauncher = mutableStateOf<ManagedActivityResultLauncher<String, Uri?>?>(null)

    val isAuth = mutableStateOf(repository.auth.currentUser == null)

    val showUsernameDialog = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    val scrollBehavior = mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior())

    val filterType = mutableStateOf(2)
    val isDescendingFilter = mutableStateOf(false)

    init {
        repository.auth.addAuthStateListener {
            if (it.currentUser == null) isAuth.value = true
        }
    }

    fun signOut() {
        showNoteCreation.targetState = false
        showGoalCreation.targetState = false
        globalNote.value = null
        globalGoal.value = null

        repository.auth.signOut()
    }

    fun dispatchSearch() {
        searchMode.value = !searchMode.value
        updateSearch()
    }

    fun updateSearch(newValue: String = "") {
        searchString.value = newValue
    }

    fun clearGlobalGoal() {
        globalGoal.value = null

    }

    fun clearGlobalNote() {
        globalNote.value = null
    }

}