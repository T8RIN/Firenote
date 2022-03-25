package ru.tech.firenote.ui.composable.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.provider.LocalToastHost
import ru.tech.firenote.ui.composable.single.text.MaterialTextField
import ru.tech.firenote.ui.composable.single.toast.sendToast
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.ui.state.UIState
import ru.tech.firenote.viewModel.auth.AuthViewModel


@ExperimentalMaterial3Api
@Composable
fun RegistrationScreen(viewModel: AuthViewModel) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordConfirm by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val isFormValid by derivedStateOf {
        email.isValid() && password.length >= 7 && passwordConfirm == password
    }

    val emailError by derivedStateOf {
        !email.isValid() && email.isNotEmpty()
    }
    val passwordError by derivedStateOf {
        password.length in 1..6
    }
    val confirmPasswordError by derivedStateOf {
        password != passwordConfirm
    }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        item {
            Text(
                text = stringResource(R.string.registration),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )

            when (val state = viewModel.signUiState.collectAsState().value) {
                is UIState.Loading ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(60.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                is UIState.Success<*> -> {
                    viewModel.goTo(Screen.LoginScreen)
                    LocalToastHost.current.sendToast(
                        Icons.Outlined.AlternateEmail,
                        stringResource(R.string.emailToVerify)
                    )
                    viewModel.resetState()
                }
                is UIState.Empty -> {
                    state.message?.let {
                        LocalToastHost.current.sendToast(Icons.Outlined.Error, it)
                        viewModel.resetState()
                    }
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        MaterialTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(text = stringResource(R.string.email)) },
                            singleLine = true,
                            isError = emailError,
                            errorText = stringResource(R.string.emailIsNotValid),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            trailingIcon = {
                                if (email.isNotBlank())
                                    IconButton(onClick = { email = "" }) {
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
                            isError = passwordError,
                            errorText = stringResource(R.string.passwordTooShort),
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
                            value = passwordConfirm,
                            onValueChange = { passwordConfirm = it },
                            label = { Text(text = stringResource(R.string.passwordConfirm)) },
                            singleLine = true,
                            isError = confirmPasswordError,
                            errorText = stringResource(R.string.passwordsDifferent),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                                if (isFormValid) viewModel.signInWith(email, password)
                            }),
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
                    }
                }
            }

            Button(
                onClick = { viewModel.signInWith(email, password) },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = stringResource(R.string.signUp))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                TextButton(onClick = {
                    viewModel.goTo(Screen.LoginScreen)
                }) {
                    Text(text = stringResource(R.string.logIn))
                }
            }
        }
    }
}