package ru.tech.firenote.ui.composable.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.tech.firenote.ui.composable.screen.base.GoalListScreen
import ru.tech.firenote.ui.composable.screen.base.NoteListScreen
import ru.tech.firenote.ui.composable.screen.base.ProfileScreen
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.viewModel.MainViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    contentPadding: PaddingValues,
    mainViewModel: MainViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteListScreen.route,
        Modifier.padding(contentPadding)
    ) {
        composable(Screen.NoteListScreen.route) {
            NoteListScreen(
                mainViewModel.showNoteCreation,
                mainViewModel.globalNote,
                mainViewModel.filterType,
                mainViewModel.isDescendingFilter
            )
        }
        composable(Screen.GoalsScreen.route) {
            GoalListScreen(mainViewModel.showGoalCreation, mainViewModel.globalGoal)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                navController,
                mainViewModel.selectedItem,
                mainViewModel.resultLauncher,
                mainViewModel.profileTitle,
                mainViewModel.showUsernameDialog
            )
        }
    }
}
