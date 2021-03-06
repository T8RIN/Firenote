package ru.tech.firenote.viewModel.creation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.tech.firenote.model.Note
import ru.tech.firenote.repository.NoteRepository
import ru.tech.firenote.ui.theme.NoteYellow
import ru.tech.firenote.utils.GlobalUtils.blend
import javax.inject.Inject

@HiltViewModel
class NoteCreationViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    var note: Note? = null

    val noteColor = mutableStateOf(NoteYellow.toArgb())
    val appBarColor = mutableStateOf(noteColor.value.blend())

    val noteLabel = mutableStateOf("")
    val noteContent = mutableStateOf("")

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
            repository.insertNote(
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
        this.note = note
        noteLabel.value = note?.title ?: ""
        noteContent.value = note?.content ?: ""
        noteColor.value = note?.color ?: NoteYellow.toArgb()
        appBarColor.value = note?.appBarColor ?: noteColor.value.blend()
    }

    fun resetValues() {
        note = null
        noteColor.value = NoteYellow.toArgb()
        appBarColor.value = noteColor.value.blend()

        noteLabel.value = ""
        noteContent.value = ""
    }

}