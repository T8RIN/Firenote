package ru.tech.firenote.ui.composable.screen.creation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.tech.firenote.R
import ru.tech.firenote.model.Note
import ru.tech.firenote.ui.composable.provider.LocalToastHost
import ru.tech.firenote.ui.composable.single.bar.EditableAppBar
import ru.tech.firenote.ui.composable.single.dialog.MaterialDialog
import ru.tech.firenote.ui.composable.single.text.EditText
import ru.tech.firenote.ui.composable.single.toast.FancyToastValues
import ru.tech.firenote.ui.composable.single.toast.sendToast
import ru.tech.firenote.ui.theme.noteColors
import ru.tech.firenote.utils.GlobalUtils.blend
import ru.tech.firenote.viewModel.creation.NoteCreationViewModel

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun NoteCreationScreen(
    state: MutableTransitionState<Boolean>,
    globalNote: MutableState<Note?>,
    reset: MutableState<Boolean>,
    viewModel: NoteCreationViewModel = viewModel()
) {
    val tempLabel = rememberSaveable { mutableStateOf("") }
    val tempContent = rememberSaveable { mutableStateOf("") }
    val tempColor = rememberSaveable { mutableStateOf(0) }

    val hasChanges by derivedStateOf {
        tempLabel.value != viewModel.noteLabel.value
                || tempContent.value != viewModel.noteContent.value
                || (tempColor.value != viewModel.noteColor.value
                && viewModel.noteContent.value.isNotEmpty()
                && viewModel.noteLabel.value.isNotEmpty())
    }

    val txt = stringResource(R.string.fillAll)
    val toastHost = LocalToastHost.current

    val scope = rememberCoroutineScope()
    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(
                if (viewModel.note != null) viewModel.noteColor.value else globalNote.value?.color
                    ?: viewModel.noteColor.value
            )
        )
    }
    val appBarAnimatable = remember {
        Animatable(
            Color(
                if (viewModel.note != null) viewModel.appBarColor.value else globalNote.value?.appBarColor
                    ?: viewModel.appBarColor.value
            )
        )
    }

    val needToShowCancelDialog = rememberSaveable { mutableStateOf(false) }

    var editionMode by rememberSaveable { mutableStateOf(globalNote.value == null) }

    Scaffold(
        topBar = {
            EditableAppBar(
                textModifier = Modifier.horizontalScroll(rememberScrollState()),
                backgroundColor = appBarAnimatable.value,
                text = viewModel.noteLabel,
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
                hint = stringResource(R.string.enterNoteLabel),
                errorColor = if (state.targetState) noteBackgroundAnimatable.value.toArgb() else Color.Transparent.toArgb(),
                color = Color.Black,
                enabled = editionMode
            ) {
                viewModel.noteLabel.value = it
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                text = {
                    Text(
                        stringResource(
                            if (editionMode) R.string.save else R.string.edit
                        )
                    )
                },
                icon = {
                    Icon(
                        if (editionMode) Icons.Outlined.Save else Icons.Outlined.Edit,
                        null
                    )
                },
                onClick = {
                    if (editionMode) {
                        saveNote(viewModel, toastHost, txt, state, globalNote.value)
                    } else {
                        editionMode = true
                    }
                })
        }
    ) { contentPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .navigationBarsPadding()
                .padding(contentPadding),
            contentPadding = PaddingValues(
                bottom = 400.dp
            )
        ) {
            item {
                AnimatedVisibility(
                    editionMode,
                    enter = fadeIn() + slideInHorizontally(),
                    exit = fadeOut() + slideOutHorizontally()
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        contentPadding = PaddingValues(15.dp)
                    ) {
                        items(noteColors.size) { index ->
                            val color = noteColors[index]
                            val colorInt = color.toArgb()

                            val endPadding = if (index == 8) 25.dp else 5.dp
                            val width = if (index == 8) 80.dp else 60.dp

                            Box(
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(width)
                                    .padding(
                                        start = 5.dp,
                                        top = 5.dp,
                                        end = endPadding,
                                        bottom = 5.dp
                                    )
                                    .shadow(15.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = 3.dp,
                                        color = if (viewModel.noteColor.value == colorInt) {
                                            Color.Black
                                        } else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        viewModel.noteColor.value = colorInt
                                        viewModel.appBarColor.value = colorInt.blend()

                                        scope.launch {
                                            noteBackgroundAnimatable.animateTo(
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

                EditText(
                    textFieldState = viewModel.noteContent,
                    topPadding = 20.dp,
                    hintText = stringResource(R.string.noteText),
                    errorColor = viewModel.noteColor.value.blend(0.7f),
                    singleLine = false,
                    color = Color.Black,
                    enabled = editionMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    viewModel.noteContent.value = it
                    if (viewModel.noteLabel.value.isEmpty() && viewModel.noteContent.value.length > 20) {
                        viewModel.noteLabel.value = it.substring(0..20)
                    }
                }
            }
        }
    }

    MaterialDialog(
        showDialog = needToShowCancelDialog,
        icon = Icons.Outlined.Save,
        title = R.string.saveNote,
        message = R.string.noteSavingDialogMessage,
        confirmText = R.string.save,
        dismissText = R.string.discardChanges,
        confirmAction = { saveNote(viewModel, toastHost, txt, state, globalNote.value) },
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

    LaunchedEffect(globalNote.value) {
        if (viewModel.note != globalNote.value) {
            viewModel.parseNoteData(globalNote.value)
            tempLabel.value = viewModel.noteLabel.value
            tempContent.value = viewModel.noteContent.value
            tempColor.value = viewModel.noteColor.value
            editionMode = globalNote.value == null
            scope.launch {
                noteBackgroundAnimatable.animateTo(
                    targetValue = Color(viewModel.noteColor.value),
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

private fun saveNote(
    viewModel: NoteCreationViewModel,
    toastHost: FancyToastValues,
    message: String,
    state: MutableTransitionState<Boolean>,
    note: Note?
) {
    if (viewModel.noteContent.value.isNotBlank() && viewModel.noteLabel.value.isNotEmpty()) {
        if (note != null) {
            viewModel.updateNote(note)
        } else {
            viewModel.saveNote()
        }
        state.targetState = false
    } else toastHost.sendToast(Icons.Outlined.Error, message)
}