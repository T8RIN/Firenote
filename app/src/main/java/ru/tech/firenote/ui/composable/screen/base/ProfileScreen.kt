package ru.tech.firenote.ui.composable.screen.base

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ru.tech.firenote.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    
}