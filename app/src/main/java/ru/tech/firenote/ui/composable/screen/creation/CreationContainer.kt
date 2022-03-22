package ru.tech.firenote.ui.composable.screen.creation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FactCheck
import androidx.compose.material.icons.twotone.StickyNote2
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.tech.firenote.R
import ru.tech.firenote.viewModel.MainViewModel

@Composable
fun CreationContainer(mainViewModel: MainViewModel, splitScreen: Boolean) {
    Box(Modifier.fillMaxSize()) {
        if (!mainViewModel.showNoteCreation.currentState) {
            mainViewModel.globalNote.value = null
            if (splitScreen && mainViewModel.selectedItem.value == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.TwoTone.StickyNote2, null, modifier = Modifier.fillMaxSize(0.3f))
                    Text(stringResource(R.string.selectNote))
                }
            }
        }
        if (!mainViewModel.showGoalCreation.currentState) {
            mainViewModel.globalGoal.value = null
            if (splitScreen && mainViewModel.selectedItem.value in 1..2) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.TwoTone.FactCheck, null, modifier = Modifier.fillMaxSize(0.3f))
                    Text(stringResource(R.string.selectGoal))
                }
            }
        }

        AnimatedVisibility(
            visibleState = mainViewModel.showNoteCreation,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { mainViewModel.showNoteCreation.targetState = false }

            NoteCreationScreen(
                state = mainViewModel.showNoteCreation,
                globalNote = mainViewModel.globalNote
            )
        }

        AnimatedVisibility(
            visibleState = mainViewModel.showGoalCreation,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { mainViewModel.showGoalCreation.targetState = false }

            GoalCreationScreen(
                state = mainViewModel.showGoalCreation,
                globalGoal = mainViewModel.globalGoal
            )
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