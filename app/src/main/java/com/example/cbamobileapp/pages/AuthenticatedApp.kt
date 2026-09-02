package com.example.cbamobileapp.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cbamobileapp.viewmodel.AuthViewModel

@Composable
fun AuthenticatedApp(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    if (authState.isSignedIn) {
        ProductivityCoachApp(
            userEmail = authState.email.orEmpty(),
            onSignOut = authViewModel::signOut
        )
    } else {
        AuthScreen(
            isLoading = authState.isLoading,
            errorMessage = authState.errorMessage,
            onSignIn = authViewModel::signIn,
            onRegister = authViewModel::register
        )
    }
}