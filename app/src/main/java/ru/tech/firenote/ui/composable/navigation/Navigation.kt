package ru.tech.firenote.ui.composable.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.tech.firenote.model.Screen
import ru.tech.firenote.ui.composable.screen.NoteListScreen

@Composable
fun Navigation(
    navController: NavHostController,
    contentPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteListScreen.route,
        Modifier.padding(contentPadding)
    ) {
        composable(Screen.NoteListScreen.route) {
            NoteListScreen()
        }
        composable(Screen.AlarmListScreen.route) {
            AlarmListScreen()
        }
    }
}

@Composable
fun AlarmListScreen() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(100) { index ->
            Text(
                "I'm item $index", modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
