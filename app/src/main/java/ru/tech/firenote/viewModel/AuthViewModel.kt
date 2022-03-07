package ru.tech.firenote.viewModel

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.tech.firenote.NoteRepository
import ru.tech.firenote.model.Screen
import ru.tech.firenote.model.UIState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    repository: NoteRepository
) : ViewModel() {

    private val auth = repository.auth
    private val currentUser get() = auth.currentUser
    val visibleState = MutableTransitionState(currentUser == null)

    private val _logUiState = MutableStateFlow<UIState>(UIState.Empty())
    val logUiState: StateFlow<UIState> = _logUiState

    private val _signUiState = MutableStateFlow<UIState>(UIState.Empty())
    val signUiState: StateFlow<UIState> = _signUiState

    val currentScreen = mutableStateOf(Screen.LoginScreen.route)

    fun logInWith(email: String, password: String) {
        _logUiState.value = UIState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (currentUser?.isEmailVerified == false) {
                        currentUser?.sendEmailVerification()
                        auth.signOut()
                        _logUiState.value = UIState.Empty("verification")
                    } else {
                        _logUiState.value = UIState.Success(currentUser)
                        visibleState.targetState = false
                    }
                } else {
                    _logUiState.value = UIState.Empty(task.exception.toString().split(":")[1])
                }
            }
    }

    fun signInWith(email: String, password: String) {
        if (currentUser == null) {
            _signUiState.value = UIState.Loading
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser?.sendEmailVerification()
                        _signUiState.value = UIState.Success(currentUser)
                        auth.signOut()
                    } else {
                        _signUiState.value = UIState.Empty(task.exception.toString().split(":")[1])
                    }
                }
        }
    }

    fun sendResetPasswordLink(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    fun resetState() {
        _signUiState.value = UIState.Empty()
        _logUiState.value = UIState.Empty()
    }

    fun goTo(screen: Screen) {
        currentScreen.value = screen.route
    }

}