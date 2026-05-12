package com.nexora.auth.domain.repository

import com.nexora.auth.domain.model.User
import com.nexora.core.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendOtp(phone: String): Result<Boolean>
    suspend fun verifyOtp(phone: String, code: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun refreshToken(): Result<String>
    suspend fun logout(): Result<Unit>
    fun getAuthState(): Flow<AuthState>
    suspend fun getCurrentUser(): Result<User>
    suspend fun isLoggedIn(): Boolean
    suspend fun resetPassword(email: String): Result<Boolean>
}

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val user: User? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val error: String? = null
)
