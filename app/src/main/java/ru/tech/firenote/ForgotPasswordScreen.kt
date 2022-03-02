package ru.tech.firenote

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }

    val isFormValid by derivedStateOf { email.isValid() }

    val emailError by derivedStateOf {
        !email.isValid() && email.isNotEmpty()
    }

    val context = LocalContext.current

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
                        text = stringResource(R.string.resetPassword),
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
                    }

                    Button(
                        onClick = { /*TODO*/
                            viewModel.currentScreen.value = Screen.LoginScreen.route
                            Toast.makeText(
                                context,
                                context.getString(R.string.checkYourEmail),
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = stringResource(R.string.sendEmail))
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