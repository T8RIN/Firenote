package ru.tech.firenote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
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
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(
                        text = stringResource(R.string.registration),
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
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
                            isError = passwordError,
                            errorText = stringResource(R.string.passwordTooShort),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
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

                    Button(
                        onClick = { /*TODO*/ },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = stringResource(R.string.signUp))
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        TextButton(onClick = {
                            viewModel.currentScreen.value = Screen.LoginScreen.route
                        }) {
                            Text(text = stringResource(R.string.logIn))
                        }
                    }
                }
            }
        }
    }
}