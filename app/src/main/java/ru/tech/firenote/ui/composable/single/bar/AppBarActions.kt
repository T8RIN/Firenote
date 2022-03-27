package ru.tech.firenote.ui.composable.single.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.provider.LocalToastHost
import ru.tech.firenote.ui.composable.single.dialog.MaterialDialog
import ru.tech.firenote.ui.composable.single.toast.sendToast
import ru.tech.firenote.utils.GlobalUtils.isOnline
import ru.tech.firenote.viewModel.main.MainViewModel

@Composable
fun NoteActions(
    viewModel: MainViewModel
) {
    val selectedModifier =
        Modifier
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
    val unselectedModifier = Modifier.padding(5.dp)
    val showFilter = remember { mutableStateOf(false) }

    IconButton(onClick = {
        viewModel.isDescendingFilter.value = !viewModel.isDescendingFilter.value
    }) {
        Icon(
            if (viewModel.isDescendingFilter.value) Icons.Outlined.ArrowDropDown else Icons.Outlined.ArrowDropUp,
            "filter"
        )
    }
    IconButton(onClick = { showFilter.value = true }) {
        Icon(Icons.Outlined.FilterAlt, null)
    }

    DropdownMenu(
        expanded = showFilter.value,
        onDismissRequest = { showFilter.value = false }
    ) {
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 0
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.title)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 0) Icons.Filled.TextSnippet else Icons.Outlined.TextSnippet,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 0) selectedModifier else unselectedModifier
        )
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 1
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.color)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 1) Icons.Filled.Palette else Icons.Outlined.Palette,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 1) selectedModifier else unselectedModifier
        )
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 2
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.date)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value in 2..3) Icons.Filled.CalendarToday else Icons.Outlined.CalendarToday,
                    null
                )
            },
            modifier = if (viewModel.filterType.value in 2..3) selectedModifier else unselectedModifier
        )
    }
}

@Composable
fun GoalActions(viewModel: MainViewModel) {
    val selectedModifier =
        Modifier
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
    val unselectedModifier = Modifier.padding(5.dp)
    val showFilter = remember { mutableStateOf(false) }

    IconButton(onClick = {
        viewModel.isDescendingFilter.value = !viewModel.isDescendingFilter.value
    }) {
        Icon(
            if (viewModel.isDescendingFilter.value) Icons.Outlined.ArrowDropDown else Icons.Outlined.ArrowDropUp,
            null
        )
    }
    IconButton(onClick = { showFilter.value = true }) {
        Icon(Icons.Outlined.FilterAlt, null)
    }

    DropdownMenu(
        expanded = showFilter.value,
        onDismissRequest = { showFilter.value = false }
    ) {
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 0
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.title)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 0) Icons.Filled.TextSnippet else Icons.Outlined.TextSnippet,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 0) selectedModifier else unselectedModifier
        )
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 1
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.color)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 1) Icons.Filled.Palette else Icons.Outlined.Palette,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 1) selectedModifier else unselectedModifier
        )
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 3
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.date)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 3) Icons.Filled.CalendarToday else Icons.Outlined.CalendarToday,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 3) selectedModifier else unselectedModifier
        )
        DropdownMenuItem(
            onClick = {
                viewModel.filterType.value = 2
                showFilter.value = false
            },
            text = { Text(stringResource(R.string.completion)) },
            leadingIcon = {
                Icon(
                    if (viewModel.filterType.value == 2) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    null
                )
            },
            modifier = if (viewModel.filterType.value == 2) selectedModifier else unselectedModifier
        )
    }
}

@Composable
fun ProfileActions(onClick: () -> Unit) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val toastHost = LocalToastHost.current
    val txt = stringResource(R.string.noInternet)

    IconButton(onClick = {
        if (context.isOnline()) showDialog.value = true
        else toastHost.sendToast(Icons.Outlined.SignalWifiOff, txt)
    }) {
        Icon(Icons.Outlined.Logout, null)
    }

    MaterialDialog(
        showDialog = showDialog,
        icon = Icons.Outlined.Logout,
        title = R.string.logOut,
        message = R.string.logoutMessage,
        confirmText = R.string.stay,
        dismissText = R.string.logOut,
        dismissAction = onClick,
        backHandler = {}
    )
}

@Composable
fun FileAttachActions(onItemSelected: (Attachments) -> Unit) {

    var showFilter by remember { mutableStateOf(false) }

    IconButton(onClick = {
        showFilter = true
    }) {
        Icon(Icons.Outlined.AttachFile, null, tint = Color.Black)
    }

    DropdownMenu(
        expanded = showFilter,
        onDismissRequest = { showFilter = false }
    ) {
        DropdownMenuItem(
            onClick = {
                showFilter = false
                onItemSelected(Attachments.IMAGE)
            },
            text = { Text(stringResource(R.string.image)) },
            leadingIcon = { Icon(Icons.Outlined.Image, null) }
        )
        DropdownMenuItem(
            onClick = {
                showFilter = false
                onItemSelected(Attachments.DOCUMENT)
            },
            text = { Text(stringResource(R.string.document)) },
            leadingIcon = { Icon(Icons.Outlined.TextSnippet, null) }
        )
        DropdownMenuItem(
            onClick = {
                showFilter = false
                onItemSelected(Attachments.AUDIO)
            },
            text = { Text(stringResource(R.string.Audio)) },
            leadingIcon = { Icon(Icons.Outlined.MusicNote, null) }
        )
        DropdownMenuItem(
            onClick = {
                showFilter = false
                onItemSelected(Attachments.FILE)
            },
            text = { Text(stringResource(R.string.File)) },
            leadingIcon = { Icon(Icons.Outlined.InsertDriveFile, null) }
        )
    }

//    val contentLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let { viewModel.addAttachment(uri) }
//    }
//
//    val documentLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.OpenDocument()
//    ) { uri: Uri? ->
//        uri?.let { viewModel.addAttachment(uri) }
//    }
//
//    FileAttachActions(onItemSelected = {
//        when (it) {
//            Attachments.IMAGE -> contentLauncher.launch("image/*")
//            Attachments.DOCUMENT -> documentLauncher.launch(arrayOf(
//                "docx", "doc", "ppt", "pptx", "xls", "xlsx", "pdf"
//            ).toMimeType())
//            Attachments.AUDIO -> contentLauncher.launch("audio/*")
//            else -> contentLauncher.launch("*/*")
//        }
//    })


    // fun Array<String>.toMimeType() : Array<String> {
    //        return this.map {
    //            MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) ?: ""
    //        }.toTypedArray()
    //    }
}

enum class Attachments {
    IMAGE, DOCUMENT, AUDIO, FILE
}
