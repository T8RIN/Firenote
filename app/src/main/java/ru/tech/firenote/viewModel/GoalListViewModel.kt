package ru.tech.firenote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.tech.firenote.model.Goal
import ru.tech.firenote.repository.NoteRepository
import ru.tech.firenote.ui.state.UIState
import javax.inject.Inject

@HiltViewModel
class GoalListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Empty())
    val uiState: StateFlow<UIState> = _uiState


    init {
        getGoals()
    }

    private fun getGoals() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading

            while (repository.auth.currentUser == null) {
                delay(500)
            }

            repository.getGoals().collect {
                if (it.isSuccess) {
                    if (it.getOrNull().isNullOrEmpty()) _uiState.value = UIState.Empty()
                    else _uiState.value = UIState.Success(it.getOrNull())
                } else {
                    _uiState.value = UIState.Empty(it.exceptionOrNull()?.localizedMessage)
                }
            }
        }
    }

    fun deleteGoal(
        goal: Goal,
        onDeleted: (Goal) -> Unit
    ) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
            onDeleted(goal)
        }
    }

    fun insertGoal(goal: Goal) {
        viewModelScope.launch { repository.insertGoal(goal) }
    }

}