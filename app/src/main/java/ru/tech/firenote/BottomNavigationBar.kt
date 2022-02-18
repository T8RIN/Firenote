package ru.tech.firenote

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<Screen>) {
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    NavigationBar(modifier = Modifier.navigationBarsPadding()) {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(screen.route) {
                        navController.popBackStack()
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}