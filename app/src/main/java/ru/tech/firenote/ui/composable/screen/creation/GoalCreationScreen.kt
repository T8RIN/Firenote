package ru.tech.firenote.ui.composable.screen.creation

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.tech.firenote.R
import ru.tech.firenote.model.Goal
import ru.tech.firenote.model.GoalData
import ru.tech.firenote.ui.composable.single.bar.EditableAppBar
import ru.tech.firenote.ui.composable.single.dialog.MaterialDialog
import ru.tech.firenote.ui.theme.goalColors
import ru.tech.firenote.utils.GlobalUtils.blend
import ru.tech.firenote.viewModel.creation.GoalCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCreationScreen(
    state: MutableTransitionState<Boolean>,
    globalGoal: MutableState<Goal?>,
    reset: MutableState<Boolean>,
    viewModel: GoalCreationViewModel = viewModel()
) {
    val tempLabel = rememberSaveable { mutableStateOf("") }
    val tempContent = rememberSaveable { mutableStateOf(listOf(GoalData(done = false))) }
    val tempColor = rememberSaveable { mutableStateOf(0) }

    val hasChanges by derivedStateOf {
        tempLabel.value != viewModel.goalLabel.value
                || !(tempContent.value.containsAll(viewModel.goalContent.value)
                && viewModel.goalContent.value.containsAll(tempContent.value))
                || (tempColor.value != viewModel.goalColor.value
                && viewModel.goalContent.value.isNotEmpty()
                && viewModel.goalLabel.value.isNotEmpty())
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val goalBackgroundAnimatable = remember {
        Animatable(
            Color(
                if (viewModel.goal != null) viewModel.goalColor.value else globalGoal.value?.color
                    ?: viewModel.goalColor.value
            )
        )
    }
    val appBarAnimatable = remember {
        Animatable(
            Color(
                if (viewModel.goal != null) viewModel.appBarColor.value else globalGoal.value?.appBarColor
                    ?: viewModel.appBarColor.value
            )
        )
    }

    val needToShowCancelDialog = rememberSaveable { mutableStateOf(false) }

    var editionMode by rememberSaveable { mutableStateOf(globalGoal.value == null) }

    Scaffold(
        topBar = {
            EditableAppBar(
                textModifier = Modifier.horizontalScroll(rememberScrollState()),
                backgroundColor = appBarAnimatable.value,
                text = viewModel.goalLabel,
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasChanges && editionMode) {
                            needToShowCancelDialog.value = true
                        } else {
                            viewModel.resetValues()
                            state.targetState = false
                        }
                    }) {
                        Icon(Icons.Rounded.ArrowBack, null, tint = Color.Black)
                    }
                },
                hint = stringResource(R.string.enterGoalLabel),
                errorColor = if (state.targetState) goalBackgroundAnimatable.value.toArgb() else Color.Transparent.toArgb(),
                color = Color.Black,
                enabled = editionMode
            ) {
                viewModel.goalLabel.value = it
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                text = {
                    Text(stringResource(if (editionMode) R.string.save else R.string.edit))
                },
                icon = {
                    Icon(
                        if (editionMode) Icons.Outlined.Save else Icons.Outlined.Edit,
                        null
                    )
                },
                onClick = {
                    if (editionMode) saveGoal(viewModel, context, state, globalGoal.value)
                    else editionMode = true
                })
        }
    ) { contentPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(goalBackgroundAnimatable.value)
                .navigationBarsPadding()
                .padding(contentPadding),
            contentPadding = PaddingValues(
                bottom = 400.dp
            )
        ) {
            if (editionMode) {
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        contentPadding = PaddingValues(15.dp)
                    ) {
                        items(goalColors.size) { index ->
                            val color = goalColors[index]
                            val colorInt = color.toArgb()
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(5.dp)
                                    .shadow(15.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = 3.dp,
                                        color = if (viewModel.goalColor.value == colorInt) {
                                            Color.Black
                                        } else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        viewModel.goalColor.value = colorInt
                                        viewModel.appBarColor.value = colorInt.blend()

                                        scope.launch {
                                            goalBackgroundAnimatable.animateTo(
                                                targetValue = Color(colorInt),
                                                animationSpec = tween(
                                                    durationMillis = 500
                                                )
                                            )
                                        }
                                        scope.launch {
                                            appBarAnimatable.animateTo(
                                                targetValue = Color(viewModel.appBarColor.value),
                                                animationSpec = tween(
                                                    durationMillis = 500
                                                )
                                            )
                                        }
                                    }
                            )
                        }
                    }
                }

                items(viewModel.goalContent.value.size) { index ->
                    Row(verticalAlignment = Alignment.Top) {

                        val text = viewModel.goalContent.value[index].content ?: ""

                        Spacer(Modifier.size(8.dp))

                        Dot(
                            Modifier
                                .size(34.dp)
                                .padding(top = 18.dp, start = 12.dp, end = 12.dp),
                            appBarAnimatable.value
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 12.dp)
                        ) {
                            BasicTextField(
                                value = text,
                                textStyle = TextStyle(
                                    fontSize = 22.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                ),
                                onValueChange = {
                                    viewModel.updateContent(index, it)
                                })
                            if (text.isEmpty()) {
                                Text(
                                    stringResource(R.string.subGoalHere),
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 12.dp),
                                    color = Color.DarkGray
                                )
                            }
                        }



                        IconButton(onClick = {
                            viewModel.removeFromContent(index)
                        }) {
                            Icon(Icons.Rounded.Close, null, tint = darkColorScheme().onTertiary)
                        }

                        Spacer(Modifier.size(8.dp))

                    }
                }

                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = appBarAnimatable.value
                            ),
                            onClick = { viewModel.addContent(GoalData(done = false)) },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(Icons.Outlined.Add, null, tint = darkColorScheme().onTertiary)
                            Spacer(Modifier.size(16.dp))
                            Text(
                                stringResource(R.string.addSubGoal),
                                color = darkColorScheme().onTertiary
                            )
                        }
                    }
                }
            } else {
                items(viewModel.goalContent.value.size) { index ->
                    Row(verticalAlignment = Alignment.Top) {

                        Spacer(Modifier.size(8.dp))

                        Checkbox(checked = viewModel.goalContent.value[index].done ?: false,
                            colors = CheckboxDefaults.colors(
                                checkedColor = appBarAnimatable.value,
                                uncheckedColor = appBarAnimatable.value,
                                checkmarkColor = Color.Black
                            ),
                            onCheckedChange = {
                                viewModel.updateDone(index, it)
                            })

                        Text(
                            text = viewModel.goalContent.value[index].content ?: "",
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 12.dp)
                                .clickable(remember { MutableInteractionSource() }, null) {
                                    viewModel.updateDone(
                                        index,
                                        !(viewModel.goalContent.value[index].done ?: false)
                                    )
                                },
                            style = TextStyle(
                                fontSize = 22.sp,
                                color = if (viewModel.goalContent.value[index].done == true) Color.DarkGray else Color.Black,
                                textAlign = TextAlign.Start,
                            ),
                            textDecoration = if (viewModel.goalContent.value[index].done == true) TextDecoration.LineThrough else TextDecoration.None
                        )

                        Spacer(Modifier.size(8.dp))

                    }
                }
            }
        }
    }

    MaterialDialog(
        showDialog = needToShowCancelDialog,
        icon = Icons.Outlined.Save,
        title = R.string.saveGoal,
        message = R.string.goalSavingDialogMessage,
        confirmText = R.string.save,
        dismissText = R.string.discardChanges,
        confirmAction = { saveGoal(viewModel, context, state, globalGoal.value) },
        dismissAction = {
            viewModel.resetValues()
            state.targetState = false
        },
        backHandler = {
            BackHandler {
                if (hasChanges && editionMode) {
                    needToShowCancelDialog.value = true
                } else {
                    viewModel.resetValues()
                    state.targetState = false
                }
            }
        }
    )

    LaunchedEffect(globalGoal.value) {
        if (viewModel.goal != globalGoal.value) {
            viewModel.parseGoalData(globalGoal.value)
            tempLabel.value = viewModel.goalLabel.value
            tempContent.value = viewModel.goalContent.value
            tempColor.value = viewModel.goalColor.value
            editionMode = globalGoal.value == null
            scope.launch {
                goalBackgroundAnimatable.animateTo(
                    targetValue = Color(viewModel.goalColor.value),
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            }
            scope.launch {
                appBarAnimatable.animateTo(
                    targetValue = Color(viewModel.appBarColor.value),
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            }
        }
    }

    if (reset.value) {
        viewModel.resetValues()
        reset.value = false
    }

}

private fun saveGoal(
    viewModel: GoalCreationViewModel,
    context: Context,
    state: MutableTransitionState<Boolean>,
    goal: Goal?
) {
    if (viewModel.goalContent.value.isNotEmpty() && viewModel.goalLabel.value.isNotEmpty()) {
        if (goal != null) {
            viewModel.updateGoal(goal)
        } else {
            viewModel.saveGoal()
        }
        state.targetState = false
    } else Toast.makeText(context, R.string.fillAll, Toast.LENGTH_SHORT).show()
}

@Composable
fun Dot(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier) {
        drawCircle(color)
    }
}