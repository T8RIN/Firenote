package ru.tech.firenote.ui.composable.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.twotone.Cloud
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.tech.firenote.R
import ru.tech.firenote.model.Note
import ru.tech.firenote.model.UIState
import ru.tech.firenote.ui.composable.single.MaterialDialog
import ru.tech.firenote.ui.composable.single.NoteItem
import ru.tech.firenote.ui.composable.single.Toast
import ru.tech.firenote.viewModel.NoteListViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val notePaddingValues = PaddingValues(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 80.dp)
    val needToShowDeleteDialog = remember { mutableStateOf(false) }
    var note by remember { mutableStateOf(Note()) }

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
            val data = state.data as List<*>

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = notePaddingValues
            ) {
                items(data.size) { index ->
                    NoteItem(
                        note = data[index] as Note,
                        onDeleteClick = {
                            note = data[index] as Note
                            needToShowDeleteDialog.value = true
                        })
                }
            }

            MaterialDialog(
                showDialog = needToShowDeleteDialog,
                icon = Icons.Outlined.Delete,
                title = R.string.deleteNote,
                message = R.string.deleteNoteMessage,
                confirmText = R.string.close,
                dismissText = R.string.delete,
                dismissAction = { viewModel.deleteNote(note) },
                backHandler = { }
            )
        }
        is UIState.Empty -> {
            state.message?.let { Toast(it) }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.TwoTone.Cloud, null, modifier = Modifier.fillMaxSize(0.3f))
                Text(stringResource(R.string.noNotes))
            }
        }
    }
}