package ru.tech.firenote.ui.composable.screen.navigation

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Password
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.provider.LocalToastHost
import ru.tech.firenote.ui.composable.provider.LocalWindowSize
import ru.tech.firenote.ui.composable.screen.auth.isValid
import ru.tech.firenote.ui.composable.single.dialog.MaterialDialog
import ru.tech.firenote.ui.composable.single.lazyitem.ProfileNoteItem
import ru.tech.firenote.ui.composable.single.text.MaterialTextField
import ru.tech.firenote.ui.composable.single.toast.sendToast
import ru.tech.firenote.ui.composable.utils.WindowSize
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.ui.state.UIState
import ru.tech.firenote.ui.theme.noteColors
import ru.tech.firenote.viewModel.navigation.ProfileViewModel

@Suppress("UNCHECKED_CAST")
@Composable
fun ProfileScreen(
    navController: NavController,
    selectedItem: MutableState<Int>,
    resultLauncher: MutableState<ManagedActivityResultLauncher<String, Uri?>?>,
    profileTitle: MutableState<String>,
    showUsernameDialog: MutableState<Boolean>,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val toastHost = LocalToastHost.current
    val configuration = LocalConfiguration.current

    val imageTxt = stringResource(R.string.imagePicked)

    var boxSize: Double =
        if (configuration.screenWidthDp <= configuration.screenHeightDp) configuration.screenWidthDp.toDouble() else configuration.screenHeightDp.toDouble()

    boxSize *= if (LocalWindowSize.current == WindowSize.Compact) 0.4 else 0.16

    resultLauncher.value = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateProfile(uri)
        uri?.let {
            toastHost.sendToast(Icons.Outlined.Image, imageTxt)
        }
    }

    val showResetDialog = remember { mutableStateOf(false) }
    val showEmailDialog = remember { mutableStateOf(false) }

    when (val state = viewModel.username.collectAsState().value) {
        is UIState.Empty -> {
            state.message?.let {
                toastHost.sendToast(Icons.Outlined.Error, it)
            }
        }
        is UIState.Success<*> -> {
            profileTitle.value = state.data as String
        }
        else -> {}
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 80.dp, top = 20.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Box(Modifier.size(boxSize.dp)) {
                val alpha = rememberSaveable { mutableStateOf(0f) }
                when (val state = viewModel.photoState.collectAsState().value) {
                    is UIState.Loading -> alpha.value = 1f
                    is UIState.Success<*> -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.data as Uri?)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            onLoading = {
                                alpha.value = 1f
                            },
                            onSuccess = {
                                alpha.value = 0f
                            },
                            onError = {
                                alpha.value = 0f
                            }
                        )
                    }
                    is UIState.Empty -> {
                        state.message?.let {
                            toastHost.sendToast(Icons.Outlined.Error, it)
                        }
                        Icon(Icons.Default.AccountCircle, null, Modifier.fillMaxSize())
                        alpha.value = 0f
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alpha.value),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            Spacer(Modifier.size(20.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.TwoTone.Email, null)
                Spacer(Modifier.size(20.dp))
                Text(
                    text = viewModel.email,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState())
                )
                TextButton(
                    modifier = Modifier.padding(start = 20.dp),
                    onClick = { showEmailDialog.value = true }) {
                    Text(
                        text = stringResource(R.string.change),
                        color = Color.Gray
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.TwoTone.Password, null)
                Spacer(Modifier.size(20.dp))
                Text(
                    text = "•••••••••••••",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState())
                )
                TextButton(modifier = Modifier.padding(start = 20.dp), onClick = {
                    showResetDialog.value = true
                }) {
                    Text(
                        text = stringResource(R.string.reset),
                        color = Color.Gray
                    )
                }
            }
            Spacer(Modifier.size(5.dp))

            when (val state = viewModel.noteCountState.collectAsState().value) {
                is UIState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UIState.Empty -> {
                    state.message?.let {
                        toastHost.sendToast(Icons.Outlined.Error, it)
                    }
                }
                is UIState.Success<*> -> {
                    FlowRow {
                        repeat(noteColors.size + 1) {
                            if (it == 9) {
                                Spacer(
                                    Modifier
                                        .fillMaxWidth(0.75f)
                                        .height(20.dp)
                                )
                            } else {
                                val index = if (it < 9) it else it - 1
                                val type =
                                    viewModel.typeState.value.firstOrNull { item -> item.color == noteColors[index].toArgb() }?.type
                                        ?: ""

                                ProfileNoteItem(
                                    pair = noteColors[index] to ((state.data as? List<Int>)?.get(
                                        index
                                    ) ?: 0),
                                    typeText = type,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxSize(0.25f),
                                    onValueChange = { newValue ->
                                        viewModel.updateType(noteColors[index], newValue)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    val txt = stringResource(R.string.checkYourEmail)

    MaterialDialog(
        icon = Icons.Outlined.Password,
        title = R.string.resetPassword,
        message = R.string.resetMessage,
        confirmText = R.string.reset,
        confirmAction = {
            toastHost.sendToast(Icons.Outlined.AlternateEmail, txt)
            viewModel.sendResetPasswordLink()
            viewModel.signOut()
            selectedItem.value = 0
            navController.navigate(Screen.NoteListScreen.route) {
                navController.popBackStack()
                launchSingleTop = true
            }
        },
        dismissText = R.string.close,
        showDialog = showResetDialog,
        backHandler = {}
    )

    if (showEmailDialog.value) {
        val focusManager = LocalFocusManager.current

        var emailOld by remember {
            mutableStateOf("")
        }
        var emailNew by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        var isPasswordVisible by remember {
            mutableStateOf(false)
        }
        val isFormValid by derivedStateOf {
            emailOld.isValid() && emailNew.isValid() && password.isNotEmpty()
        }

        AlertDialog(
            icon = { Icon(Icons.Outlined.Email, null) },
            title = { Text(stringResource(R.string.changeEmail)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.changeEmail(emailOld, password, emailNew)
                }, enabled = isFormValid) {
                    Text(stringResource(R.string.change))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEmailDialog.value = false
                }) {
                    Text(stringResource(R.string.close))
                }
            },
            text = {
                val emailOldError by derivedStateOf {
                    !emailOld.isValid() && emailOld.isNotEmpty()
                }
                val emailNewError by derivedStateOf {
                    !emailNew.isValid() && emailNew.isNotEmpty()
                }

                when (val state = viewModel.updateState.collectAsState().value) {
                    is UIState.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is UIState.Empty -> {
                        state.message?.let {
                            toastHost.sendToast(Icons.Outlined.Error, it)
                        }
                        Column(
                            Modifier
                                .wrapContentHeight()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            MaterialTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = emailOld,
                                onValueChange = { emailOld = it },
                                label = { Text(text = stringResource(R.string.email)) },
                                singleLine = true,
                                isError = emailOldError,
                                errorText = stringResource(R.string.emailIsNotValid),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                trailingIcon = {
                                    if (emailOld.isNotBlank())
                                        IconButton(onClick = { emailOld = "" }) {
                                            Icon(Icons.Filled.Clear, null)
                                        }
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            MaterialTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = password,
                                onValueChange = { password = it },
                                label = { Text(text = stringResource(R.string.password)) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Next
                                ),
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        isPasswordVisible = !isPasswordVisible
                                    }) {
                                        Icon(
                                            if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            null
                                        )
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            MaterialTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = emailNew,
                                onValueChange = { emailNew = it },
                                label = { Text(text = stringResource(R.string.newEmail)) },
                                singleLine = true,
                                isError = emailNewError,
                                errorText = stringResource(R.string.emailIsNotValid),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    focusManager.clearFocus()
                                    if (isFormValid) viewModel.changeEmail(
                                        emailOld,
                                        password,
                                        emailNew
                                    )
                                }),
                                trailingIcon = {
                                    if (emailOld.isNotBlank())
                                        IconButton(onClick = { emailOld = "" }) {
                                            Icon(Icons.Filled.Clear, null)
                                        }
                                }
                            )
                        }
                    }
                    is UIState.Success<*> -> {
                        toastHost.sendToast(
                            Icons.Outlined.DoneOutline,
                            stringResource(R.string.emailChanged)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                        viewModel.sendVerifyEmail()
                        viewModel.signOut()
                        selectedItem.value = 0
                        navController.navigate(Screen.NoteListScreen.route) {
                            navController.popBackStack()
                            launchSingleTop = true
                        }
                    }
                }
            },
            onDismissRequest = { }
        )
    }

    if (showUsernameDialog.value) {
        val focusManager = LocalFocusManager.current
        var username by remember { mutableStateOf(profileTitle.value) }

        val text = stringResource(R.string.usernameChanged)

        AlertDialog(
            icon = { Icon(Icons.Outlined.Edit, null) },
            title = { Text(stringResource(R.string.changeUsername)) },
            confirmButton = {
                TextButton(onClick = {
                    toastHost.sendToast(Icons.Outlined.DoneOutline, text + username)
                    showUsernameDialog.value = false
                    viewModel.updateUsername(username)
                }) {
                    Text(stringResource(R.string.change))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showUsernameDialog.value = false
                }) {
                    Text(stringResource(R.string.close))
                }
            },
            text = {
                MaterialTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = stringResource(R.string.username)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        viewModel.updateUsername(username)
                    }),
                    trailingIcon = {
                        if (username.isNotBlank())
                            IconButton(onClick = { username = "" }) {
                                Icon(Icons.Filled.Clear, null)
                            }
                    }
                )
            },
            onDismissRequest = { showUsernameDialog.value = false }
        )
    }
}