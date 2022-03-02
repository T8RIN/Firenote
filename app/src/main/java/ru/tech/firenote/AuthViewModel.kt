package ru.tech.firenote

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    dataStore: DataStore<Preferences>
) : ViewModel() {

    private val auth = Firebase.auth

    private val _uiState = MutableStateFlow<UIState>(UIState.Empty)
    val uiState: StateFlow<UIState> = _uiState

    val currentScreen = mutableStateOf(Screen.LoginScreen.route)

    fun logInWith(email: String, password: String) {
        _uiState.value = UIState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = UIState.Success(auth.currentUser)
                    currentScreen.value = Screen.NoteListScreen.route
                } else {
//                    Toast.makeText(
//                        context, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }

    }


}