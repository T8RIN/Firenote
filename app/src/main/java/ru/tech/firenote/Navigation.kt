package ru.tech.firenote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun Navigation(navController: NavHostController, contentPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteListScreen.route,
        Modifier.padding(contentPadding)
    ) {
        composable(Screen.NoteListScreen.route) {
            NoteListScreen(navController)
        }
        composable(
            "${Screen.NoteScreen.route}/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) {
            NoteScreen(navController)
        }
        composable(Screen.NoteCreationScreen.route) {
            NoteCreationScreen(navController)
        }
        composable(Screen.CalendarScreen.route) {
            CalendarScreen(navController)
        }
    }
}

@Composable
fun NoteListScreen(navController: NavHostController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(100) { index ->
            Text("I'm item $index", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp))
        }
    }
}

@Composable
fun NoteScreen(navController: NavHostController) {

}

@Composable
fun NoteCreationScreen(navController: NavHostController) {

}

@Composable
fun CalendarScreen(navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        CustomIcon(Icons.Filled.CalendarToday)
    }
}
