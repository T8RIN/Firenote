package ru.tech.firenote

sealed class UIState {
    class Empty(val message: String? = null) : UIState()
    object Loading : UIState()
    class Success<T>(val data: T) : UIState()
}