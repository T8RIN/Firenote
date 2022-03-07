package ru.tech.firenote.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tech.firenote.NoteRepository
import ru.tech.firenote.Utils.blend
import ru.tech.firenote.model.Note
import ru.tech.firenote.ui.theme.*
import javax.inject.Inject

@HiltViewModel
class NoteCreationViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val noteColor = mutableStateOf(NoteYellow.toArgb())
    val appBarColor = mutableStateOf(noteColor.value.blend())

    val noteLabel = mutableStateOf("")
    val noteContent = mutableStateOf("")

    val colors =
        listOf(NoteYellow, NoteGreen, NoteMint, NoteBlue, NoteViolet, NoteOrange, NoteRed, NotePink)

    fun saveNote() {
        viewModelScope.launch {
            repository.insertNote(
                Note(
                    noteLabel.value,
                    noteContent.value,
                    System.currentTimeMillis(),
                    noteColor.value,
                    appBarColor.value
                )
            )
            resetValues()
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(
                Note(
                    noteLabel.value,
                    noteContent.value,
                    System.currentTimeMillis(),
                    noteColor.value,
                    appBarColor.value,
                    note.id
                )
            )
            resetValues()
        }
    }

    fun parseNoteData(note: Note?) {
        noteLabel.value = note?.title ?: ""
        noteContent.value = note?.content ?: ""
        noteColor.value = note?.color ?: NoteYellow.toArgb()
        appBarColor.value = note?.appBarColor ?: noteColor.value.blend()
    }

    fun resetValues() {
        viewModelScope.launch {
            delay(500)
            noteColor.value = NoteYellow.toArgb()
            appBarColor.value = noteColor.value.blend()

            noteLabel.value = ""
            noteContent.value = ""
        }
    }

}