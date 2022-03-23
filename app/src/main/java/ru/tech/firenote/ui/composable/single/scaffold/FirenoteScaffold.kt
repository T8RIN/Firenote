package ru.tech.firenote.ui.composable.single.scaffold

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.navigation.Navigation
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.single.bar.*
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.viewModel.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirenoteScaffold(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavHostController,
    context: Context
) {
    Scaffold(
        topBar = {
            AppBarWithInsets(
                type = APP_BAR_CENTER,
                navigationIcon = {
                    when (viewModel.selectedItem.value) {
                        in 0..1 -> {
                            IconButton(onClick = {
                                viewModel.dispatchSearch()
                            }) {
                                Icon(
                                    if (!viewModel.searchMode.value) Icons.Rounded.Search else Icons.Rounded.ArrowBack,
                                    null
                                )
                            }
                        }
                        2 -> {
                            IconButton(onClick = {
                                viewModel.showUsernameDialog.value = true
                            }) {
                                Icon(Icons.Outlined.Edit, null)
                            }
                        }
                    }
                },
                scrollBehavior = viewModel.scrollBehavior.value,
                title = {
                    if (!viewModel.searchMode.value) {
                        Text(
                            text = if (viewModel.selectedItem.value == 2) viewModel.profileTitle.value
                            else stringResource(viewModel.title.value),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        SearchBar(searchString = viewModel.searchString.value) {
                            viewModel.updateSearch(it)
                        }
                    }
                },
                actions = {
                    if (!viewModel.searchMode.value) {
                        when (viewModel.selectedItem.value) {
                            0 -> NoteActions(viewModel)
                            1 -> GoalActions(viewModel)
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

                                viewModel.signOut()
                            }
                        }
                    } else {
                        IconButton(onClick = {
                            viewModel.updateSearch()
                        }) {
                            Icon(Icons.Rounded.Close, null)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                when (viewModel.selectedItem.value) {
                    0 -> {
                        viewModel.showNoteCreation.targetState = true
                        viewModel.clearGlobalNote()
                    }
                    1 -> {
                        viewModel.showGoalCreation.targetState = true
                        viewModel.clearGlobalGoal()
                    }
                    2 -> viewModel.resultLauncher.value?.launch("image/*")
                }
            }, icon = {
                Icon(
                    when (viewModel.selectedItem.value) {
                        0 -> Icons.Outlined.Edit
                        1 -> Icons.Outlined.AddTask
                        2 -> Icons.Outlined.Image
                        else -> Icons.Outlined.Error
                    }, null
                )
            }, text = {
                Text(
                    when (viewModel.selectedItem.value) {
                        0 -> stringResource(R.string.addNote)
                        1 -> stringResource(R.string.makeGoal)
                        2 -> stringResource(R.string.pickImage)
                        else -> ""
                    }
                )
            })
        },
        bottomBar = {
            BottomNavigationBar(
                title = viewModel.title,
                selectedItem = viewModel.selectedItem,
                searchMode = viewModel.searchMode,
                searchString = viewModel.searchString,
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
        modifier = modifier.nestedScroll(viewModel.scrollBehavior.value.nestedScrollConnection)
    ) { contentPadding ->
        Navigation(navController, contentPadding, viewModel)
    }
}