package ru.tech.firenote.ui.composable.screen.navigation

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.twotone.Cloud
import androidx.compose.material.icons.twotone.FindInPage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.tech.firenote.R
import ru.tech.firenote.model.Note
import ru.tech.firenote.ui.composable.provider.LocalLazyListStateProvider
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.provider.LocalToastHost
import ru.tech.firenote.ui.composable.provider.showSnackbar
import ru.tech.firenote.ui.composable.single.dialog.MaterialDialog
import ru.tech.firenote.ui.composable.single.lazyitem.NoteItem
import ru.tech.firenote.ui.composable.single.placeholder.Placeholder
import ru.tech.firenote.ui.composable.single.toast.sendToast
import ru.tech.firenote.ui.state.UIState
import ru.tech.firenote.ui.theme.priority
import ru.tech.firenote.viewModel.navigation.NoteListViewModel

@Suppress("UNCHECKED_CAST")
@ExperimentalFoundationApi
@Composable
fun NoteListScreen(
    showNoteCreation: MutableTransitionState<Boolean>,
    globalNote: MutableState<Note?> = mutableStateOf(null),
    filterType: MutableState<Int>,
    isDescendingFilter: MutableState<Boolean>,
    searchString: MutableState<String>,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val notePaddingValues = PaddingValues(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 140.dp)
    val needToShowDeleteDialog = remember { mutableStateOf(false) }
    var note by remember { mutableStateOf(Note()) }
    val scope = rememberCoroutineScope()
    val host = LocalSnackbarHost.current

    val message = stringResource(R.string.noteDeleted)
    val action = stringResource(R.string.undo)

    when (val state = viewModel.uiState.collectAsState().value) {
        is UIState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        is UIState.Success<*> -> {
            val repoList = state.data as List<Note>
            var data = if (isDescendingFilter.value) {
                when (filterType.value) {
                    0 -> repoList.sortedBy { it.title }
                    1 -> repoList.sortedBy { (it.color ?: 0).priority }
                    else -> repoList.sortedBy { it.timestamp }
                }
            } else {
                when (filterType.value) {
                    0 -> repoList.sortedByDescending { it.title }
                    1 -> repoList.sortedByDescending { (it.color ?: 0).priority }
                    else -> repoList.sortedByDescending { it.timestamp }
                }
            }

            if (searchString.value.isNotEmpty()) {
                data = repoList.filter {
                    it.content?.lowercase()?.contains(searchString.value)
                        ?.or(
                            it.title?.lowercase()?.contains(searchString.value) ?: false
                        ) ?: false
                }
                if (data.isEmpty()) {
                    Placeholder(icon = Icons.TwoTone.FindInPage, textRes = R.string.nothingFound)
                }
            }

            LazyColumn(
                state = LocalLazyListStateProvider.current,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = notePaddingValues
            ) {
                items(data.size) { index ->
                    val locNote = data[index]
                    NoteItem(
                        note = locNote,
                        onDeleteClick = {
                            note = locNote
                            needToShowDeleteDialog.value = true
                        },
                        modifier = Modifier
                            .clickable(remember { MutableInteractionSource() }, null) {
                                globalNote.value = locNote
                                showNoteCreation.targetState = true
                            }
                    )
                }
            }
        }
        is UIState.Empty -> {
            state.message?.let {
                LocalToastHost.current.sendToast(Icons.Outlined.Error, it)
            }
            Placeholder(icon = Icons.TwoTone.Cloud, textRes = R.string.noNotes)
        }
    }

    MaterialDialog(
        showDialog = needToShowDeleteDialog,
        icon = Icons.Outlined.Delete,
        title = R.string.deleteNote,
        message = R.string.deleteNoteMessage,
        confirmText = R.string.close,
        dismissText = R.string.delete,
        dismissAction = {
            viewModel.deleteNote(note) { note ->
                var temp = note.title.toString().take(30)
                if (note.title.toString().length > 30) temp += "..."
                val messageNew = message.replace("*", temp)

                showSnackbar(
                    scope,
                    host,
                    messageNew,
                    action
                ) {
                    if (it == SnackbarResult.ActionPerformed) {
                        viewModel.insertNote(note)
                    }
                }
            }
        },
        backHandler = { }
    )
}