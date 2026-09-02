package com.example.cbamobileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cbamobileapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val userId: String? = null,
    val email: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isSignedIn: Boolean
        get() = userId != null
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(
            userId = authRepository.currentUserId,
            email = authRepository.currentUserEmail
        )
    )

    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(email: String, password: String) {
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            try {
                authRepository.register(
                    email = email.trim(),
                    password = password
                )

                updateSignedInUser()
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "Unable to create the account."
                    )
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            try {
                authRepository.signIn(
                    email = email.trim(),
                    password = password
                )

                updateSignedInUser()
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "Unable to sign in."
                    )
                }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()

        _uiState.value = AuthUiState()
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun updateSignedInUser() {
        _uiState.value = AuthUiState(
            userId = authRepository.currentUserId,
            email = authRepository.currentUserEmail
        )
    }

    private fun validate(
        email: String,
        password: String
    ): Boolean {
        val error = when {
            email.isBlank() ->
                "Please enter your email address."

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Please enter a valid email address."

            password.length < 6 ->
                "The password must contain at least 6 characters."

            else -> null
        }

        if (error != null) {
            _uiState.update {
                it.copy(errorMessage = error)
            }

            return false
        }

        return true
    }
}