package ru.tech.firenote.ui.composable.screen.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.tech.firenote.R
import ru.tech.firenote.ui.composable.provider.LocalWindowSize
import ru.tech.firenote.ui.composable.utils.WindowSize
import ru.tech.firenote.ui.route.Screen
import ru.tech.firenote.viewModel.auth.AuthViewModel

@ExperimentalMaterial3Api
@Composable
fun AuthScreen(visible: MutableState<Boolean>, viewModel: AuthViewModel = viewModel()) {

    if (viewModel.currentUser == null) {
        viewModel.visibleState.targetState = true
        viewModel.resetState()
    }
    visible.value = viewModel.visibleState.targetState
    AnimatedVisibility(
        visibleState = viewModel.visibleState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
        ) {
            Column(
                Modifier
                    .fillMaxSize(),
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
                        .weight(
                            when (LocalWindowSize.current) {
                                WindowSize.Compact -> 2f
                                else -> 1f
                            }
                        )
                        .padding(
                            when (LocalWindowSize.current) {
                                WindowSize.Compact -> 12.dp
                                WindowSize.Medium -> 48.dp
                                else -> 96.dp
                            }
                        ),
                    shape = RoundedCornerShape(24.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box {
                        when (viewModel.currentScreen.value) {
                            Screen.LoginScreen.route -> LoginScreen(viewModel)
                            Screen.RegistrationScreen.route -> RegistrationScreen(viewModel)
                            Screen.ForgotPasswordScreen.route -> ForgotPasswordScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}