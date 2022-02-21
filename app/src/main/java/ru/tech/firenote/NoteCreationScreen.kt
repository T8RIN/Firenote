package ru.tech.firenote

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCreationScreen(
    state: MutableState<Boolean>,
    viewModel: NoteCreationViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            AppBarWithInsets(
                navigationIcon = {
                    IconButton(onClick = { state.value = false }) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                modifier = Modifier.background(
                    TopAppBarDefaults.smallTopAppBarColors().containerColor(
                        scrollFraction = 10f
                    ).value
                ),
                title = stringResource(R.string.createNote),
                actions = {
                    viewModel.noteCreationActions()
                }
            )
        }
    ) {

    }
}