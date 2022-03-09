package ru.tech.firenote.ui.composable.screen.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import ru.tech.firenote.model.Screen
import ru.tech.firenote.model.UIState
import ru.tech.firenote.ui.composable.single.MaterialTextField
import ru.tech.firenote.ui.composable.single.Toast
import ru.tech.firenote.viewModel.AuthViewModel


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
        email.isValid()
    }

    val emailError by derivedStateOf {
        !email.isValid() && email.isNotEmpty()
    }

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(R.drawable.ic_fire_144),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.6f)
            )
            Card(
                Modifier
                    .weight(2f)
                    .padding(8.dp),
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                elevation = cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(
                        text = stringResource(R.string.welcomeBack),
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    when (val state = viewModel.logUiState.collectAsState().value) {
                        is UIState.Loading ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
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
                                    .weight(1f),
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
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = stringResource(R.string.logIn))
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
    }

}

fun String.isValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}