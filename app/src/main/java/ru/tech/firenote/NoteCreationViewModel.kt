package ru.tech.firenote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteCreationViewModel @Inject constructor(dataStore: DataStore<Preferences>) : ViewModel() {

    val noteCreationActions: @Composable () -> Unit = {
        NoteCreationActions()
    }

}