package ru.tech.firenote.ui.composable.screen.creation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FactCheck
import androidx.compose.material.icons.twotone.StickyNote2
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.single.placeholder.Placeholder
import ru.tech.firenote.viewModel.main.MainViewModel

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun CreationContainer(viewModel: MainViewModel, splitScreen: Boolean) {
    Box(Modifier.fillMaxSize()) {

        if (!viewModel.showNoteCreation.currentState) {
            viewModel.clearGlobalNote()
            if (splitScreen && viewModel.selectedItem.value == 0) {
                Placeholder(icon = Icons.TwoTone.StickyNote2, textRes = R.string.selectNote)
            }
        }

        if (!viewModel.showGoalCreation.currentState) {
            viewModel.clearGlobalGoal()
            if (splitScreen && viewModel.selectedItem.value in 1..2) {
                Placeholder(icon = Icons.TwoTone.FactCheck, textRes = R.string.selectGoal)
            }
        }

        val resetGoal = rememberSaveable { mutableStateOf(false) }
        val resetNote = rememberSaveable { mutableStateOf(false) }

        AnimatedVisibility(
            visibleState = viewModel.showNoteCreation,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { viewModel.showNoteCreation.targetState = false }

            NoteCreationScreen(
                state = viewModel.showNoteCreation,
                globalNote = viewModel.globalNote,
                reset = resetNote
            )

            LaunchedEffect(Unit) {
                viewModel.showGoalCreation.targetState = false
                resetGoal.value = true
            }
        }

        AnimatedVisibility(
            visibleState = viewModel.showGoalCreation,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { viewModel.showGoalCreation.targetState = false }

            GoalCreationScreen(
                state = viewModel.showGoalCreation,
                globalGoal = viewModel.globalGoal,
                reset = resetGoal
            )

            LaunchedEffect(Unit) {
                viewModel.showNoteCreation.targetState = false
                resetNote.value = true
            }
        }

        if (splitScreen) {
            Divider(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}