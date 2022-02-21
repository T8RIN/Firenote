package ru.tech.firenote

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object PreferenceKeys {
    val VIEW_COMPOSITION = intPreferencesKey("view")

    fun DataStore<Preferences>.setViewComposition(int: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            edit { it[VIEW_COMPOSITION] = int }
        }
    }

    fun Int.getIcon(): ImageVector = when (this) {
        0 -> Icons.Filled.ViewList
        1 -> Icons.Filled.ViewCompact
        else -> Icons.Rounded.GridView
    }
}