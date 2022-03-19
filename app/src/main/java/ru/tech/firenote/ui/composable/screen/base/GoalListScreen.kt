package ru.tech.firenote.ui.composable.screen.base

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.twotone.Cloud
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.tech.firenote.R
import ru.tech.firenote.model.Goal
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.provider.showSnackbar
import ru.tech.firenote.ui.composable.single.GoalItem
import ru.tech.firenote.ui.composable.single.MaterialDialog
import ru.tech.firenote.ui.composable.single.Toast
import ru.tech.firenote.ui.state.UIState
import ru.tech.firenote.ui.theme.priorityGoal
import ru.tech.firenote.viewModel.GoalListViewModel

@Suppress("UNCHECKED_CAST")
@Composable
fun GoalListScreen(
    showGoalCreation: MutableTransitionState<Boolean>,
    globalGoal: MutableState<Goal?> = mutableStateOf(null),
    filterType: MutableState<Int>,
    isDescendingFilter: MutableState<Boolean>,
    viewModel: GoalListViewModel = hiltViewModel()
) {
    val paddingValues = PaddingValues(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 140.dp)
    val needToShowDeleteDialog = remember { mutableStateOf(false) }
    var goal by remember { mutableStateOf(Goal()) }
    val scope = rememberCoroutineScope()
    val host = LocalSnackbarHost.current

    val message = stringResource(R.string.goalDeleted)
    val action = stringResource(R.string.undo)

    when (val state = viewModel.uiState.collectAsState().value) {
        is UIState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        is UIState.Success<*> -> {
            val repoList = state.data as List<Goal>
            val data = if (isDescendingFilter.value) {
                when (filterType.value) {
                    1 -> repoList.sortedBy { (it.color ?: 0).priorityGoal }
                    3 -> repoList.sortedBy { it.timestamp }
                    2 -> repoList.sortedByDescending {
                        var cnt = 0f
                        it.content?.forEach { data ->
                            if (data.done == true) cnt++
                        }
                        cnt / (it.content?.size ?: 1)
                    }
                    else -> repoList.sortedBy { it.title }
                }
            } else {
                when (filterType.value) {
                    1 -> repoList.sortedByDescending { (it.color ?: 0).priorityGoal }
                    3 -> repoList.sortedByDescending { it.timestamp }
                    2 -> repoList.sortedBy {
                        var cnt = 0f
                        it.content?.forEach { data ->
                            if (data.done == true) cnt++
                        }
                        cnt / (it.content?.size ?: 1)
                    }
                    else -> repoList.sortedByDescending { it.title }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = paddingValues
            ) {
                items(data.size) { index ->
                    val locGoal = data[index]
                    GoalItem(
                        cutCornerSize = 0.dp,
                        goal = locGoal,
                        onDeleteClick = {
                            goal = locGoal
                            needToShowDeleteDialog.value = true
                        },
                        modifier = Modifier
                            .clickable(remember { MutableInteractionSource() }, null) {
                                globalGoal.value = locGoal
                                showGoalCreation.targetState = true
                            }
                    )
                }
            }
        }
        is UIState.Empty -> {
            state.message?.let { Toast(it) }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.TwoTone.Cloud, null, modifier = Modifier.fillMaxSize(0.3f))
                Text(stringResource(R.string.noGoals))
            }
        }
    }

    MaterialDialog(
        showDialog = needToShowDeleteDialog,
        icon = Icons.Outlined.Delete,
        title = R.string.deleteGoal,
        message = R.string.deleteGoalMessage,
        confirmText = R.string.close,
        dismissText = R.string.delete,
        dismissAction = {
            viewModel.deleteGoal(goal) { goal1 ->
                var messageNew = message.replace("*", goal1.title.toString()).take(30)
                if (goal1.title.toString().length > 30) messageNew += "..."
                showSnackbar(
                    scope,
                    host,
                    messageNew,
                    action
                ) {
                    if (it == SnackbarResult.ActionPerformed) {
                        viewModel.insertGoal(goal1)
                    }
                }
            }
        },
        backHandler = { }
    )
}