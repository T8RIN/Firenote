package ru.tech.firenote.ui.composable.screen.auth

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import ru.tech.firenote.ui.composable.single.text.MaterialTextField
import ru.tech.firenote.ui.composable.single.toast.Toast
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.ui.state.UIState
import ru.tech.firenote.viewModel.auth.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val isFormValid by derivedStateOf {
        email.isValid() && password.isNotEmpty()
    }

    val emailError by derivedStateOf {
        !email.isValid() && email.isNotEmpty()
    }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        item {
            Text(
                text = stringResource(R.string.welcomeBack),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )

            when (val state = viewModel.logUiState.collectAsState().value) {
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
                    Toast(R.string.niceToSeeYou)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(60.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UIState.Empty -> {
                    if (state.message == "verification") {
                        Toast(R.string.notVerified)
                        viewModel.resetState()
                    } else {
                        state.message?.let {
                            Toast(it)
                            viewModel.resetState()
                        }
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
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                                if (isFormValid) viewModel.logInWith(email, password)
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
                onClick = { viewModel.logInWith(email, password) },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = stringResource(R.string.logIn))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    viewModel.goTo(Screen.RegistrationScreen)
                }) {
                    Text(text = stringResource(R.string.signUp))
                }
                TextButton(onClick = {
                    viewModel.goTo(Screen.ForgotPasswordScreen)
                }) {
                    Text(
                        text = stringResource(R.string.forgotPassword),
                        color = Color.Gray
                    )
                }
            }
        }
    }

}

fun String.isValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}