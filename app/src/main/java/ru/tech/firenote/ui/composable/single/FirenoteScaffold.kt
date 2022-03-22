package ru.tech.firenote.ui.composable.single

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.navigation.Navigation
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.single.bar.*
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirenoteScaffold(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    context: Context
) {
    Scaffold(
        topBar = {
            AppBarWithInsets(
                type = APP_BAR_CENTER,
                navigationIcon = {
                    when (mainViewModel.selectedItem.value) {
                        0 -> {
                            if (!mainViewModel.searchMode.value) {
                                IconButton(onClick = {
                                    mainViewModel.searchMode
                                        .value = true
                                    mainViewModel.searchString.value =
                                        ""

                                }) {
                                    Icon(Icons.Rounded.Search, null)
                                }
                            } else {
                                IconButton(onClick = {
                                    mainViewModel.searchMode
                                        .value = false
                                    mainViewModel.searchString.value =
                                        ""
                                }) {
                                    Icon(Icons.Rounded.ArrowBack, null)
                                }
                            }
                        }
                        1 -> {
                            if (!mainViewModel.searchMode.value) {
                                IconButton(onClick = {
                                    mainViewModel.searchMode
                                        .value = true
                                    mainViewModel.searchString.value =
                                        ""

                                }) {
                                    Icon(Icons.Rounded.Search, null)
                                }
                            } else {
                                IconButton(onClick = {
                                    mainViewModel.searchMode
                                        .value = false
                                    mainViewModel.searchString.value =
                                        ""
                                }) {
                                    Icon(Icons.Rounded.ArrowBack, null)
                                }
                            }
                        }
                        2 -> {
                            IconButton(onClick = {
                                mainViewModel.showUsernameDialog.value =
                                    true
                            }) {
                                Icon(Icons.Outlined.Edit, null)
                            }
                        }
                    }
                },
                scrollBehavior = mainViewModel.scrollBehavior.value,
                title = {
                    if (!mainViewModel.searchMode.value) {
                        Text(
                            text = if (mainViewModel.selectedItem.value == 2) mainViewModel.profileTitle.value
                            else stringResource(mainViewModel.title.value),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        val localFocusManager =
                            LocalFocusManager.current
                        BasicTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = mainViewModel.searchString.value,
                            textStyle = TextStyle(
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Start,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { localFocusManager.clearFocus() }
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                            onValueChange = {
                                mainViewModel.searchString.value = it
                            })
                        if (mainViewModel.searchString.value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.searchHere),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp),
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    color = MaterialTheme.colorScheme.outline,
                                    textAlign = TextAlign.Start,
                                )
                            )
                        }
                    }
                },
                actions = {
                    if (!mainViewModel.searchMode.value) {
                        when (mainViewModel.selectedItem.value) {
                            0 -> NoteActions(mainViewModel)
                            1 -> GoalActions(mainViewModel)
                            2 -> ProfileActions {
                                navController.navigate(Screen.NoteListScreen.route) {
                                    navController.popBackStack()
                                    launchSingleTop = true
                                }
                                android.widget.Toast.makeText(
                                    context,
                                    R.string.seeYouAgain,
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()

                                mainViewModel.signOut()
                            }
                        }
                    } else {
                        IconButton(onClick = {
                            mainViewModel.searchString.value = ""
                        }) {
                            Icon(Icons.Rounded.Close, null)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                when (mainViewModel.selectedItem.value) {
                    0 -> {
                        mainViewModel.showNoteCreation.targetState =
                            true
                        mainViewModel.globalNote.value = null
                    }
                    1 -> {
                        mainViewModel.showGoalCreation.targetState =
                            true
                        mainViewModel.globalGoal.value = null
                    }
                    2 -> mainViewModel.resultLauncher.value?.launch("image/*")
                }
            }, icon = {
                when (mainViewModel.selectedItem.value) {
                    0 -> Icon(Icons.Outlined.Edit, null)
                    1 -> Icon(Icons.Outlined.AddTask, null)
                    2 -> Icon(Icons.Outlined.Image, null)
                }
            }, text = {
                when (mainViewModel.selectedItem.value) {
                    0 -> Text(stringResource(R.string.addNote))
                    1 -> Text(stringResource(R.string.makeGoal))
                    2 -> Text(stringResource(R.string.pickImage))
                }
            })
        },
        bottomBar = {
            BottomNavigationBar(
                title = mainViewModel.title,
                selectedItem = mainViewModel.selectedItem,
                searchMode = mainViewModel.searchMode,
                searchString = mainViewModel.searchString,
                navController = navController,
                items = listOf(
                    Screen.NoteListScreen,
                    Screen.GoalsScreen,
                    Screen.ProfileScreen
                ),
                alwaysShowLabel = false
            )
        },
        snackbarHost = { SnackbarHost(LocalSnackbarHost.current) },
        modifier = modifier.nestedScroll(mainViewModel.scrollBehavior.value.nestedScrollConnection)
    ) { contentPadding ->
        Navigation(navController, contentPadding, mainViewModel)
    }
}