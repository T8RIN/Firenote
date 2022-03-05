package ru.tech.firenote.ui.composable.single

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import ru.tech.firenote.model.Screen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<Screen>,
    title: MutableState<Int>,
    selectedItem: MutableState<Int>
) {
    Surface(color = TopAppBarDefaults.smallTopAppBarColors().containerColor(100f).value) {
        NavigationBar(modifier = Modifier.navigationBarsPadding()) {
            items.forEachIndexed { index, screen ->
                NavigationBarItem(
                    icon = {
                        if (selectedItem.value == index) Icon(
                            screen.selectedIcon,
                            contentDescription = null
                        )
                        else Icon(screen.icon, contentDescription = null)
                    },
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = selectedItem.value == index,
                    onClick = {
                        title.value = screen.resourceId
                        selectedItem.value = index
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
}