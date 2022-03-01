package ru.tech.firenote

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.tech.firenote.ui.theme.*
import javax.inject.Inject

@HiltViewModel
class NoteCreationViewModel @Inject constructor(dataStore: DataStore<Preferences>) : ViewModel() {

    val noteColor = mutableStateOf(NoteYellow.toArgb())
    val appBarColor = mutableStateOf(NoteYellowDark.toArgb())
    val errorColor = mutableStateOf(YellowError)

    var noteLabel = mutableStateOf("")
    var noteDescription = mutableStateOf("")

    val colors = listOf(NoteYellow, NoteGreen, NoteBlue, NoteViolet, NotePink)
    val darkColors =
        listOf(NoteYellowDark, NoteGreenDark, NoteBlueDark, NoteVioletDark, NotePinkDark)

    fun saveNote() {

    }

}