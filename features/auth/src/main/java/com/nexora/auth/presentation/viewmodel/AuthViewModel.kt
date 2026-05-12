package com.nexora.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.auth.domain.model.OtpState
import com.nexora.auth.domain.model.User
import com.nexora.auth.domain.repository.AuthRepository
import com.nexora.core.logger.NexoraLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val currentStep: AuthStep = AuthStep.SPLASH,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isSignUp: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val isAuthenticated: Boolean = false
)

enum class AuthStep {
    SPLASH,
    ONBOARDING,
    EMAIL_INPUT,
    PASSWORD_INPUT,
    HOME
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val logger = NexoraLogger("AuthViewModel")
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        viewModelScope.launch {
            _uiState.update { it.copy(currentStep = AuthStep.SPLASH, isLoading = true) }
            
            val isLoggedIn = authRepository.isLoggedIn()
            logger.info("AuthViewModel", "isLoggedIn: $isLoggedIn")
            
            if (isLoggedIn) {
                val result = authRepository.getCurrentUser()
                result.onSuccess { user ->
                    _uiState.update { it.copy(isAuthenticated = true, currentStep = AuthStep.HOME, user = user, isLoading = false) }
                }.onError {
                    _uiState.update { it.copy(currentStep = AuthStep.ONBOARDING, isLoading = false) }
                }
            } else {
                _uiState.update { it.copy(currentStep = AuthStep.ONBOARDING, isLoading = false) }
            }
        }
    }
    
    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }
    
    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }
    
    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, error = null) }
    }
    
    fun goToOnboarding() {
        _uiState.update { it.copy(currentStep = AuthStep.ONBOARDING) }
    }
    
    fun goToEmailInput(isSignUp: Boolean) {
        _uiState.update { it.copy(currentStep = AuthStep.EMAIL_INPUT, isSignUp = isSignUp, email = "", password = "", error = null) }
    }
    
    fun goToPasswordInput() {
        val email = _uiState.value.email
        if (!isValidEmail(email)) {
            _uiState.update { it.copy(error = "Please enter a valid email address") }
            return
        }
        _uiState.update { it.copy(currentStep = AuthStep.PASSWORD_INPUT, error = null) }
    }
    
    fun signIn() {
        val email = _uiState.value.email
        val password = _uiState.value.password
        
        if (!validatePassword(password)) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = authRepository.signInWithEmail(email, password)
            result.onSuccess { user ->
                _uiState.update { it.copy(user = user, isAuthenticated = true, currentStep = AuthStep.HOME, isLoading = false) }
                logger.info("AuthViewModel", "Sign in successful: ${user.id}")
            }.onError { error ->
                _uiState.update { it.copy(error = "Invalid email or password", isLoading = false) }
                logger.error("AuthViewModel", "Sign in failed", error)
            }
        }
    }
    
    fun signUp() {
        val email = _uiState.value.email
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        
        if (!validatePassword(password)) return
        
        if (password != confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = authRepository.signUpWithEmail(email, password)
            result.onSuccess { user ->
                _uiState.update { it.copy(user = user, isAuthenticated = true, currentStep = AuthStep.HOME, isLoading = false) }
                logger.info("AuthViewModel", "Sign up successful: ${user.id}")
            }.onError { error ->
                _uiState.update { it.copy(error = "Failed to create account. Email may already be in use.", isLoading = false) }
                logger.error("AuthViewModel", "Sign up failed", error)
            }
        }
    }
    
    fun signInWithGoogle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // TODO: Implement Google Sign In
            _uiState.update { it.copy(isLoading = false, error = "Google Sign In not implemented yet") }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { AuthUiState(currentStep = AuthStep.ONBOARDING) }
            logger.info("AuthViewModel", "User logged out")
        }
    }
    
    private fun validatePassword(password: String): Boolean {
        return when {
            password.length < 6 -> {
                _uiState.update { it.copy(error = "Password must be at least 6 characters") }
                false
            }
            else -> true
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
