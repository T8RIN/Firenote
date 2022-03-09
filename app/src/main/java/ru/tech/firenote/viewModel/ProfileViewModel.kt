package ru.tech.firenote.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.tech.firenote.NoteRepository
import ru.tech.firenote.model.UIState
import ru.tech.firenote.ui.theme.noteColors
import ru.tech.firenote.ui.theme.priority
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

    init {
        loadProfileImage()
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            val tempList = ArrayList(List(noteColors.size) { 0 })

            _noteCountState.value = UIState.Loading
            repository.getNotes().collect {
                if (it.isSuccess) {
                    val list = it.getOrNull()
                    if (list.isNullOrEmpty()) _noteCountState.value = UIState.Success(tempList)
                    else {
                        for (note in list) {
                            tempList[(note.color ?: 0).priority]++
                        }
                        _noteCountState.value = UIState.Success(tempList)
                    }
                } else {
                    _noteCountState.value = UIState.Empty(it.exceptionOrNull()?.localizedMessage)
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
                else _photoState.value = UIState.Empty()
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

    fun sendResetPasswordLink() {
        repository.auth.sendPasswordResetEmail(email)
    }

    fun sendVerifyEmail() {
        repository.auth.currentUser?.sendEmailVerification()
    }

    fun signOut() {
        repository.auth.signOut()
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
}