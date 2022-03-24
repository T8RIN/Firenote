package ru.tech.firenote.ui.composable.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.provider.LocalToastHost
import ru.tech.firenote.ui.composable.single.text.MaterialTextField
import ru.tech.firenote.ui.composable.single.toast.sendToast
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.viewModel.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }

    val isFormValid by derivedStateOf { email.isValid() }

    val emailError by derivedStateOf {
        !email.isValid() && email.isNotEmpty()
    }

    val toastHost = LocalToastHost.current
    val focusManager = LocalFocusManager.current

    LazyColumn(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        item {
            Text(
                text = stringResource(R.string.resetPassword),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
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
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        if (isFormValid) viewModel.sendResetPasswordLink(email)
                    }),
                    trailingIcon = {
                        if (email.isNotBlank())
                            IconButton(onClick = { email = "" }) {
                                Icon(Icons.Filled.Clear, null)
                            }
                    }
                )
            }

            val txt = stringResource(R.string.checkYourEmail)

            Button(
                onClick = {
                    viewModel.goTo(Screen.LoginScreen)
                    viewModel.sendResetPasswordLink(email)
                    toastHost.sendToast(Icons.Outlined.AlternateEmail, txt)
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = stringResource(R.string.sendEmail))
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