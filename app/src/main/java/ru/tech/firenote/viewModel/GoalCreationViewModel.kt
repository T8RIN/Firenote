package ru.tech.firenote.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.tech.firenote.model.Goal
import ru.tech.firenote.model.GoalData
import ru.tech.firenote.repository.NoteRepository
import ru.tech.firenote.ui.theme.GoalGreen
import ru.tech.firenote.ui.theme.NoteYellow
import ru.tech.firenote.utils.GlobalUtils.blend
import javax.inject.Inject

@HiltViewModel
class GoalCreationViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    var goal: Goal? = null

    val goalColor = mutableStateOf(GoalGreen.toArgb())
    val appBarColor = mutableStateOf(goalColor.value.blend())

    val goalLabel = mutableStateOf("")
    val goalContent = mutableStateOf(listOf(GoalData(done = false)))

    fun saveGoal() {
        viewModelScope.launch {
            repository.insertGoal(
                Goal(
                    goalLabel.value,
                    goalContent.value.filter {
                        !it.content?.trim().isNullOrEmpty()
                    }.map {
                        it.copy(content = it.content?.trim())
                    },
                    System.currentTimeMillis(),
                    goalColor.value,
                    appBarColor.value
                )
            )
            resetValues()
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            repository.insertGoal(
                Goal(
                    goalLabel.value,
                    goalContent.value.filter {
                        !it.content?.trim().isNullOrEmpty()
                    }.map {
                        it.copy(content = it.content?.trim())
                    },
                    System.currentTimeMillis(),
                    goalColor.value,
                    appBarColor.value,
                    goal.id
                )
            )
            resetValues()
        }
    }

    fun parseGoalData(goal: Goal?) {
        this.goal = goal
        goalLabel.value = goal?.title ?: ""
        goalContent.value = goal?.content ?: listOf(GoalData(done = false))
        goalColor.value = goal?.color ?: NoteYellow.toArgb()
        appBarColor.value = goal?.appBarColor ?: goalColor.value.blend()
    }

    fun resetValues() {
        goal = null
        goalColor.value = GoalGreen.toArgb()
        appBarColor.value = goalColor.value.blend()

        goalLabel.value = ""
        goalContent.value = listOf(GoalData(done = false))
    }

    fun removeFromContent(index: Int) {
        goalContent.value = goalContent.value.filterIndexed { index1, _ -> index1 != index }
    }

    fun updateContent(index: Int, content: String) {
        goalContent.value = goalContent.value.mapIndexed { index1, goalData ->
            if (index1 == index) {
                goalData.copy(content = content)
            } else goalData
        }
    }

    fun updateDone(index: Int, done: Boolean) {
        goalContent.value = goalContent.value.mapIndexed { index1, goalData ->
            if (index1 == index) {
                goalData.copy(done = done)
            } else goalData
        }
        viewModelScope.launch {
            goal?.copy(content = goalContent.value)?.let {
                repository.insertGoal(it)
            }
        }
    }

    fun addContent(item: GoalData) {
        goalContent.value = goalContent.value + item
    }
}