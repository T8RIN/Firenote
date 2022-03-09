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
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val email get() = repository.auth.currentUser?.email ?: ""

    private val _photoState = MutableStateFlow<UIState>(UIState.Empty())
    val photoState: StateFlow<UIState> = _photoState

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
        viewModelScope.launch {
            _photoState.value = UIState.Loading
            if (uri != null) repository.setProfileUri(uri)
            else _photoState.value = UIState.Empty()
        }
    }

    fun sendResetPasswordLink() {
        repository.auth.sendPasswordResetEmail(email)
    }

    init {
        loadProfileImage()
    }

}