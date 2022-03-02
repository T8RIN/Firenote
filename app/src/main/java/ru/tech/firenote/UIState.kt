package ru.tech.firenote

sealed class UIState {
    object Empty : UIState()
    object Loading : UIState()
    class Success<T>(val data: T) : UIState()
    class Error(val message: String) : UIState()
}