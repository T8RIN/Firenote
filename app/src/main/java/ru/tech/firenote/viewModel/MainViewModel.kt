package ru.tech.firenote.viewModel

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.tech.firenote.NoteRepository
import ru.tech.firenote.model.Note
import ru.tech.firenote.model.Screen
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val title = mutableStateOf(Screen.NoteListScreen.resourceId)

    val selectedItem = mutableStateOf(0)
    val globalNote: MutableState<Note?> = mutableStateOf(null)
    val showCreationComposable = MutableTransitionState(false).apply {
        targetState = false
    }

    val isAuth = mutableStateOf(repository.auth.currentUser == null)

    @OptIn(ExperimentalMaterial3Api::class)
    val scrollBehavior = mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior())

    val filterType = mutableStateOf(2)

    init {
        repository.auth.addAuthStateListener {
            if (it.currentUser == null) isAuth.value = true
        }
    }

    fun signOut() {
        repository.auth.signOut()
        selectedItem.value = 0
    }

}