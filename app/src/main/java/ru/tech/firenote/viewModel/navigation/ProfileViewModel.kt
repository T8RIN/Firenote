package ru.tech.firenote.viewModel.navigation

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.tech.firenote.model.Type
import ru.tech.firenote.repository.NoteRepository
import ru.tech.firenote.ui.state.UIState
import ru.tech.firenote.ui.theme.noteColors
import ru.tech.firenote.ui.theme.position
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val email get() = repository.auth.currentUser?.email ?: ""

    private val _photoState = MutableStateFlow<UIState>(UIState.Empty())
    val photoState: StateFlow<UIState> = _photoState

    private val _updateState = MutableStateFlow<UIState>(UIState.Empty())
    val updateState: StateFlow<UIState> = _updateState

    private val _noteCountState = MutableStateFlow<UIState>(UIState.Empty())
    val noteCountState: StateFlow<UIState> = _noteCountState

    private val _username = MutableStateFlow<UIState>(UIState.Success(email.split("@")[0]))
    var username: StateFlow<UIState> = _username

    private val _typeState = mutableStateOf<List<Type>>(listOf())
    val typeState: State<List<Type>> = _typeState

    init {
        loadUsername()
        loadProfileImage()
        getNotes()
        getTypes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            _noteCountState.value = UIState.Loading
            repository.getNotes().collect {
                if (it.isSuccess) {
                    val tempList = ArrayList(List(noteColors.size) { 0 })
                    val list = it.getOrNull()
                    if (list.isNullOrEmpty()) _noteCountState.value = UIState.Success(tempList)
                    else {
                        for (note in list) {
                            tempList[(note.color ?: 0).position]++
                        }
                        _noteCountState.value = UIState.Success(tempList)
                    }
                } else {
                    _noteCountState.value = UIState.Empty(it.exceptionOrNull()?.localizedMessage)
                }
            }
        }
    }

    private fun getTypes() {
        viewModelScope.launch {
            repository.getTypes().collect {
                if (it.isSuccess) {
                    val list = it.getOrNull()
                    _typeState.value = list ?: listOf()
                } else {
                    _typeState.value = listOf()
                }
            }
        }
    }

    private fun loadProfileImage() {
        viewModelScope.launch {
            _photoState.value = UIState.Loading
            repository.getProfileUri().collect {
                val profileImageUri = it.getOrNull()

                if (profileImageUri != null) _photoState.value = UIState.Success(profileImageUri)
                else _photoState.value = UIState.Empty(it.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun updateProfile(uri: Uri?) {
        if (uri != null) {
            viewModelScope.launch {
                _photoState.value = UIState.Loading
                repository.setProfileUri(uri)
            }
        }
    }

    private fun loadUsername() {
        viewModelScope.launch {
            _username.value = UIState.Loading
            repository.getUsername().collect {
                val username = it.getOrNull()

                if (username != null) _username.value = UIState.Success(username)
                else _username.value = UIState.Empty(it.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun sendResetPasswordLink() {
        repository.auth.sendPasswordResetEmail(email)
    }

    fun sendVerifyEmail() {
        repository.auth.currentUser?.sendEmailVerification()
    }

    fun signOut() {
        repository.auth.signOut()
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            _username.value = UIState.Loading
            repository.setUsername(username)
        }
    }

    fun changeEmail(oldEmail: String, password: String, newEmail: String) {
        _updateState.value = UIState.Loading
        repository.auth.signInWithEmailAndPassword(oldEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    repository.auth.currentUser?.updateEmail(newEmail)
                        ?.addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                _updateState.value = UIState.Success(newEmail)
                            } else {
                                _updateState.value =
                                    UIState.Empty(emailTask.exception?.localizedMessage)
                            }
                        }
                } else {
                    _updateState.value = UIState.Empty(task.exception?.localizedMessage)
                }
            }
    }

    fun updateType(color: Color, type: String) {
        viewModelScope.launch {
            repository.updateType(color.toArgb(), type)
        }
    }
}