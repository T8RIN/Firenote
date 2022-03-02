package ru.tech.firenote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AuthScreen(viewModel: AuthViewModel = viewModel()) {

    if (Firebase.auth.currentUser == null) {
        when(viewModel.currentScreen.value) {
            Screen.LoginScreen.route -> LoginScreen(viewModel)
            Screen.RegistrationScreen.route -> RegistrationScreen(viewModel)
            Screen.ForgotPasswordScreen.route -> ForgotPasswordScreen(viewModel)
        }

    }
}