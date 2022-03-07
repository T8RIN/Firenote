package ru.tech.firenote.ui.composable.screen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.launch
import ru.tech.firenote.R
import ru.tech.firenote.model.Note
import ru.tech.firenote.ui.composable.single.EditText
import ru.tech.firenote.ui.composable.single.EditableAppBar
import ru.tech.firenote.ui.composable.single.Gradient
import ru.tech.firenote.ui.composable.single.MaterialDialog
import ru.tech.firenote.viewModel.NoteCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCreationScreen(
    state: MutableTransitionState<Boolean>,
    globalNote: MutableState<Note?> = mutableStateOf(null),
    viewModel: NoteCreationViewModel = viewModel()
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(globalNote.value?.color ?: viewModel.noteColor.value)
        )
    }
    val appBarAnimatable = remember {
        Animatable(
            Color(globalNote.value?.appBarColor ?: viewModel.appBarColor.value)
        )
    }

    val gradientColor = rememberSaveable { mutableStateOf(noteBackgroundAnimatable.value.toArgb()) }

    val needToShowCancelDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            EditableAppBar(
                modifier = Modifier.background(appBarAnimatable.value),
                text = viewModel.noteLabel,
                navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.noteDescription.value.isNotEmpty()) {
                            needToShowCancelDialog.value = true
                        } else {
                            state.targetState = false
                            viewModel.resetValues()
                        }
                    }) {
                        Icon(Icons.Rounded.ArrowBack, null, tint = Color.Black)
                    }
                },
                hint = stringResource(R.string.enterNoteLabel),
                errorColor = viewModel.errorColor,
                color = Color.Black
            ) {
                viewModel.noteLabel.value = it
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                text = { Text(stringResource(R.string.save)) },
                icon = { Icon(Icons.Outlined.Save, null) },
                onClick = { saveNote(viewModel, context, state, globalNote.value) })
        }
    ) { contentPadding ->
        Column(
            Modifier
                .background(noteBackgroundAnimatable.value)
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(contentPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                viewModel.colors.forEachIndexed { index, color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
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
                                gradientColor.value = Color.Transparent.toArgb()
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                    gradientColor.value = colorInt
                                }
                                scope.launch {
                                    appBarAnimatable.animateTo(
                                        targetValue = viewModel.darkColors[index],
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.noteColor.value = colorInt
                                viewModel.appBarColor.value =
                                    viewModel.darkColors[index].toArgb()
                                viewModel.setColors()
                            }
                    )
                }
            }
            Box(modifier = Modifier.wrapContentHeight()) {
                EditText(
                    textFieldState = viewModel.noteDescription,
                    topPadding = 20.dp,
                    endPaddingIcon = 20.dp,
                    hintText = stringResource(R.string.noteText),
                    errorColor = viewModel.errorColor,
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(
                            start = 30.dp,
                            end = 30.dp,
                        ),
                    color = Color.Black
                ) {
                    viewModel.noteDescription.value = it
                    if (viewModel.noteLabel.value.isEmpty() && viewModel.noteDescription.value.length > 20) {
                        viewModel.noteLabel.value = it.substring(0..20)
                    }
                }
                Gradient(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    Color(gradientColor.value),
                    Color.Transparent
                )
                Gradient(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .align(Alignment.BottomCenter),
                    Color.Transparent,
                    Color(gradientColor.value)
                )
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
        confirmAction = { saveNote(viewModel, context, state, globalNote.value) },
        dismissAction = {
            viewModel.resetValues()
            state.targetState = false
        },
        backHandler = {
            BackHandler {
                if (viewModel.noteDescription.value.isNotEmpty()) {
                    needToShowCancelDialog.value = true
                } else {
                    viewModel.resetValues()
                    state.targetState = false
                }
            }
        }
    )

    LaunchedEffect(Unit) { viewModel.parseNoteData(globalNote.value) }
}

fun saveNote(
    viewModel: NoteCreationViewModel,
    context: Context,
    state: MutableTransitionState<Boolean>,
    note: Note?
) {
    if (viewModel.noteDescription.value.isNotBlank() && viewModel.noteLabel.value.isNotEmpty()) {
        if (note != null) {
            viewModel.updateNote(note)
        } else {
            viewModel.saveNote()
        }
        state.targetState = false
    } else Toast.makeText(context, R.string.fillAll, Toast.LENGTH_SHORT).show()
}