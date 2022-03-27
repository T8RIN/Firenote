package ru.tech.firenote.ui.composable.single.scaffold

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.navigation.Navigation
import ru.tech.firenote.ui.composable.provider.LocalLazyListStateProvider
import ru.tech.firenote.ui.composable.provider.LocalSnackbarHost
import ru.tech.firenote.ui.composable.provider.LocalToastHost
import ru.tech.firenote.ui.composable.single.ExtendableFloatingActionButton
import ru.tech.firenote.ui.composable.single.bar.*
import ru.tech.firenote.ui.composable.single.toast.sendToast
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.utils.GlobalUtils.isOnline
import ru.tech.firenote.viewModel.main.MainViewModel

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
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

                    AnimatedVisibility(
                        viewModel.selectedItem.value in 0..1 && !viewModel.searchMode.value,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        IconButton(onClick = {
                            viewModel.dispatchSearch()
                        }) {
                            Icon(
                                Icons.Rounded.Search,
                                null
                            )
                        }
                    }

                    AnimatedVisibility(
                        viewModel.selectedItem.value in 0..1 && viewModel.searchMode.value,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        IconButton(onClick = {
                            viewModel.dispatchSearch()
                        }) {
                            Icon(
                                Icons.Rounded.ArrowBack,
                                null
                            )
                        }
                    }

                    AnimatedVisibility(
                        viewModel.selectedItem.value == 2,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    )
                    {
                        val toastHost = LocalToastHost.current
                        val txt = stringResource(R.string.noInternet)
                        Row {
                            IconButton(onClick = {
                                if (context.isOnline()) viewModel.resultLauncher.value?.launch("image/*")
                                else toastHost.sendToast(Icons.Outlined.SignalWifiOff, txt)
                            }) {
                                Icon(
                                    Icons.Outlined.AddPhotoAlternate,
                                    null
                                )
                            }

                            IconButton(onClick = {
                                viewModel.showUsernameDialog.value = true
                            }) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    null
                                )
                            }
                        }
                    }
                },
                scrollBehavior = viewModel.scrollBehavior.value,
                title = {
                    AnimatedVisibility(
                        !viewModel.searchMode.value,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            text = if (viewModel.selectedItem.value == 2) viewModel.profileTitle.value
                            else stringResource(viewModel.title.value),
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    AnimatedVisibility(
                        viewModel.searchMode.value,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        SearchBar(searchString = viewModel.searchString.value) {
                            viewModel.updateSearch(it)
                        }
                    }
                },
                actions = {
                    if (!viewModel.searchMode.value) {
                        val toastHost = LocalToastHost.current
                        val txt = stringResource(R.string.seeYouAgain)

                        when (viewModel.selectedItem.value) {
                            0 -> NoteActions(viewModel)
                            1 -> GoalActions(viewModel)
                            2 -> ProfileActions {
                                navController.navigate(Screen.NoteListScreen.route) {
                                    navController.popBackStack()
                                    launchSingleTop = true
                                }

                                toastHost.sendToast(Icons.Outlined.TagFaces, txt)

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

            val lazyListState = LocalLazyListStateProvider.current
            var fabExtended by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(lazyListState) {
                var prev = 0
                snapshotFlow { lazyListState.firstVisibleItemIndex }
                    .collect {
                        fabExtended = it <= prev
                        prev = it
                    }
            }

            AnimatedVisibility(
                visible = viewModel.selectedItem.value in 0..1,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                var icon by remember { mutableStateOf(Icons.Outlined.Edit) }
                var text by remember { mutableStateOf("") }

                when (viewModel.selectedItem.value) {
                    0 -> {
                        icon = Icons.Outlined.Edit
                        text = stringResource(R.string.addNote)
                    }
                    1 -> {
                        icon = Icons.Outlined.AddTask
                        text = stringResource(R.string.makeGoal)
                    }
                }

                ExtendableFloatingActionButton(
                    onClick = {
                        when (viewModel.selectedItem.value) {
                            0 -> {
                                viewModel.showNoteCreation.targetState = true
                                viewModel.clearGlobalNote()
                            }
                            1 -> {
                                viewModel.showGoalCreation.targetState = true
                                viewModel.clearGlobalGoal()
                            }
                        }
                    },
                    icon = { Icon(icon, null) },
                    text = { Text(text) },
                    extended = fabExtended
                )
            }
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