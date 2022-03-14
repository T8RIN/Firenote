package ru.tech.firenote.ui.route

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector
import ru.tech.firenote.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int = R.string.app_name,
    val icon: ImageVector = Icons.Default.PhoneAndroid,
    val selectedIcon: ImageVector = Icons.Default.PhoneAndroid
) {
    object NoteListScreen :
        Screen("notes", R.string.notes, Icons.Outlined.StickyNote2, Icons.Filled.StickyNote2)

    object GoalsScreen : Screen(
        "goals",
        R.string.goals,
        Icons.Outlined.FactCheck,
        Icons.Filled.FactCheck
    )

    object ProfileScreen : Screen(
        "profile",
        R.string.profile,
        Icons.Outlined.AccountCircle,
        Icons.Filled.AccountCircle
    )

    object LoginScreen : Screen("login")
    object RegistrationScreen : Screen("register")
    object ForgotPasswordScreen : Screen("forgot")

}
