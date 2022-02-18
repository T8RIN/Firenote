package ru.tech.firenote

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Notes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object NoteListScreen : Screen("notes", R.string.notes, Icons.Filled.Notes)
    object NoteCreationScreen : Screen("creation", R.string.createNote, Icons.Filled.EditNote)
    object NoteScreen : Screen("note", R.string.note, Icons.Filled.Note)
    object CalendarScreen : Screen("calendar", R.string.calendar, Icons.Filled.CalendarToday)
}
