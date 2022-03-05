package ru.tech.firenote.model

sealed class UIState {
    class Empty(var message: String? = null) : UIState()
    object Loading : UIState()
    class Success<T>(val data: T) : UIState()
}