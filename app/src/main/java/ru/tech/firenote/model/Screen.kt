package ru.tech.firenote.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.NotificationsActive
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

    object AlarmListScreen : Screen(
        "alarms",
        R.string.alarms,
        Icons.Outlined.NotificationsActive,
        Icons.Filled.NotificationsActive
    )


    object LoginScreen : Screen("login")
    object RegistrationScreen : Screen("register")
    object ForgotPasswordScreen : Screen("forgot")


//    object NoteCreationScreen : Screen("creation", R.string.createNote, Icons.Filled.EditNote)
//    object NoteScreen : Screen("note", R.string.note, Icons.Filled.Note)


}
