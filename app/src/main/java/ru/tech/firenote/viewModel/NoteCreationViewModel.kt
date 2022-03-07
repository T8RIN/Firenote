package ru.tech.firenote.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.tech.firenote.NoteRepository
import ru.tech.firenote.model.Note
import ru.tech.firenote.ui.theme.*
import javax.inject.Inject

@HiltViewModel
class NoteCreationViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val noteColor = mutableStateOf(NoteYellow.toArgb())
    val appBarColor = mutableStateOf(NoteYellowDark.toArgb())
    val errorColor = mutableStateOf(YellowError)

    val noteLabel = mutableStateOf("")
    val noteDescription = mutableStateOf("")

    val colors = listOf(NoteYellow, NoteGreen, NoteBlue, NoteViolet, NotePink)
    val darkColors =
        listOf(NoteYellowDark, NoteGreenDark, NoteBlueDark, NoteVioletDark, NotePinkDark)

    fun saveNote() {
        viewModelScope.launch {
            repository.insertNote(
                Note(
                    noteLabel.value,
                    noteDescription.value,
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
                    noteDescription.value,
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
        noteDescription.value = note?.content ?: ""
        noteColor.value = note?.color ?: NoteYellow.toArgb()
        appBarColor.value = note?.appBarColor ?: NoteYellowDark.toArgb()
        setColors()
    }

    fun resetValues() {
        noteColor.value = NoteYellow.toArgb()
        appBarColor.value = NoteYellowDark.toArgb()
        errorColor.value = YellowError

        noteLabel.value = ""
        noteDescription.value = ""
    }

    fun setColors() {
        errorColor.value = when (noteColor.value) {
            NoteYellow.toArgb() -> YellowError
            NoteGreen.toArgb() -> GreenError
            NoteBlue.toArgb() -> BlueError
            NoteViolet.toArgb() -> VioletError
            NotePink.toArgb() -> PinkError
            else -> Color(0)
        }
    }

}