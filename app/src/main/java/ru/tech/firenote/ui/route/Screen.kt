package ru.tech.firenote.ui.route

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.ui.graphics.vector.ImageVector
import ru.tech.firenote.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int = R.string.app_name,
    val baseIcon: ImageVector = Icons.Outlined.PhoneAndroid,
    val selectedIcon: ImageVector = Icons.Rounded.PhoneAndroid
) {
    object NoteListScreen : Screen(
        route = "notes",
        resourceId = R.string.notes,
        baseIcon = Icons.Outlined.StickyNote2,
        selectedIcon = Icons.Filled.StickyNote2
    )

    object GoalsScreen : Screen(
        route = "goals",
        resourceId = R.string.goals,
        baseIcon = Icons.Outlined.FactCheck,
        selectedIcon = Icons.Filled.FactCheck
    )

    object ProfileScreen : Screen(
        route = "profile",
        resourceId = R.string.profile,
        baseIcon = Icons.Outlined.AccountCircle,
        selectedIcon = Icons.Filled.AccountCircle
    )

    object LoginScreen : Screen(route = "login")
    object RegistrationScreen : Screen(route = "register")
    object ForgotPasswordScreen : Screen(route = "forgot")

}
