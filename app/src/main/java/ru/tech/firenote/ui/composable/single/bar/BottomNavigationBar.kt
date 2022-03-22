package ru.tech.firenote.ui.composable.single.bar

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import ru.tech.firenote.ui.route.Screen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<Screen>,
    searchMode: MutableState<Boolean>,
    searchString: MutableState<String>,
    title: MutableState<Int>,
    selectedItem: MutableState<Int>,
    alwaysShowLabel: Boolean = true
) {
    Surface(color = TopAppBarDefaults.smallTopAppBarColors().containerColor(100f).value) {
        NavigationBar(modifier = Modifier.navigationBarsPadding()) {
            items.forEachIndexed { index, screen ->

                selectedItem.value =
                    items.indexOfFirst { it.route == navController.currentDestination?.route }

                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem.value == index) screen.selectedIcon else screen.icon,
                            null
                        )
                    },
                    alwaysShowLabel = alwaysShowLabel,
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = selectedItem.value == index,
                    onClick = {
                        if (selectedItem.value != index) {
                            title.value = screen.resourceId
                            selectedItem.value = index
                            navController.navigate(screen.route) {
                                navController.popBackStack()
                                launchSingleTop = true
                            }
                            searchMode.value = false
                            searchString.value = ""
                        }
                    }
                )
            }
        }
    }
}