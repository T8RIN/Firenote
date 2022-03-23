package ru.tech.firenote.ui.composable.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.tech.firenote.ui.composable.screen.navigation.GoalListScreen
import ru.tech.firenote.ui.composable.screen.navigation.NoteListScreen
import ru.tech.firenote.ui.composable.screen.navigation.ProfileScreen
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.viewModel.main.MainViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    contentPadding: PaddingValues,
    viewModel: MainViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteListScreen.route,
        Modifier.padding(contentPadding)
    ) {
        composable(Screen.NoteListScreen.route) {
            NoteListScreen(
                viewModel.showNoteCreation,
                viewModel.globalNote,
                viewModel.filterType,
                viewModel.isDescendingFilter,
                viewModel.searchString
            )
        }
        composable(Screen.GoalsScreen.route) {
            GoalListScreen(
                viewModel.showGoalCreation,
                viewModel.globalGoal,
                viewModel.filterType,
                viewModel.isDescendingFilter,
                viewModel.searchString
            )
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                navController,
                viewModel.selectedItem,
                viewModel.resultLauncher,
                viewModel.profileTitle,
                viewModel.showUsernameDialog
            )
        }
    }
}
