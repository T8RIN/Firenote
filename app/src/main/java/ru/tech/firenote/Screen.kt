package ru.tech.firenote

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object NoteListScreen :
        Screen("notes", R.string.notes, Icons.Outlined.StickyNote2, Icons.Filled.StickyNote2)

//    object NoteCreationScreen : Screen("creation", R.string.createNote, Icons.Filled.EditNote)
//    object NoteScreen : Screen("note", R.string.note, Icons.Filled.Note)

    object AlarmListScreen : Screen(
        "alarms",
        R.string.alarms,
        Icons.Outlined.NotificationsActive,
        Icons.Filled.NotificationsActive
    )
}
