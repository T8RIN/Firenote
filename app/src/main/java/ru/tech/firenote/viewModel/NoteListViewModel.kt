package ru.tech.firenote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.tech.firenote.NoteRepository
import ru.tech.firenote.model.Note
import ru.tech.firenote.model.UIState
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Empty())
    val uiState: StateFlow<UIState> = _uiState


    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            repository.getNotes().collect {
                if (it.isSuccess) {
                    if (it.getOrNull().isNullOrEmpty()) _uiState.value = UIState.Empty()
                    else _uiState.value = UIState.Success(it.getOrNull())
                } else {
                    _uiState.value = UIState.Empty(it.exceptionOrNull()?.localizedMessage)
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

}