package ru.tech.firenote

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(
    navController: NavHostController,
    dataStore: DataStore<Preferences>,
    viewIcon: MutableState<Int>,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteListScreen.route,
        Modifier.padding(contentPadding)
    ) {
        composable(Screen.NoteListScreen.route) {
            NoteListScreen(navController, dataStore, viewIcon)
        }
        composable(Screen.AlarmListScreen.route) {
            AlarmListScreen()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    navController: NavHostController,
    dataStore: DataStore<Preferences>,
    viewType: MutableState<Int>
) {
    when (viewType.value) {
        0 -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(100) { index ->
                    Row {
                        Text(
                            "I'm item $index", modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .weight(1f)
                        )
                        Icon(
                            Icons.Filled.SettingsApplications,
                            null,
                            modifier = Modifier.weight(0.2f)
                        )
                    }
                }
            }
        }
        1 -> {
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
        2 -> {
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                cells = GridCells.Fixed(2)
            ) {
                items(100) { index ->
                    Text(
                        "I'm item $index", modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                    )
                }
            }
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
