package com.nexora.auth.data.repository

import com.nexora.auth.domain.model.User
import com.nexora.auth.domain.model.UserStatus
import com.nexora.auth.domain.repository.AuthRepository
import com.nexora.auth.domain.repository.AuthState
import com.nexora.core.logger.NexoraLogger
import com.nexora.core.network.ApiClient
import com.nexora.core.util.Result
import io.supabase.gotrue.SupabaseAuthClient
import io.supabase.gotrue.types.User as SupabaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authClient: SupabaseAuthClient,
    private val apiClient: ApiClient
) : AuthRepository {
    
    private val logger = NexoraLogger("AuthRepository")
    
    override suspend fun sendOtp(phone: String): Result<Boolean> {
        return Result.Success(true)
    }
    
    override suspend fun verifyOtp(phone: String, code: String): Result<User> {
        return Result.Error(Exception("Use signInWithEmail instead"))
    }
    
    override suspend fun signUpWithEmail(email: String, password: String): Result<User> {
        return try {
            logger.info("AuthRepository", "Signing up with email: $email")
            
            val response = authClient.signUp(
                email = email,
                password = password
            )
            
            val user = mapSupabaseUser(response.user!!)
            logger.info("AuthRepository", "Sign up successful: ${user.id}")
            Result.Success(user)
        } catch (e: Exception) {
            logger.error("AuthRepository", "Sign up failed", e)
            Result.Error(e)
        }
    }
    
    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            logger.info("AuthRepository", "Signing in with email: $email")
            
            val response = authClient.signIn(
                email = email,
                password = password
            )
            
            val user = mapSupabaseUser(response.user!!)
            logger.info("AuthRepository", "Sign in successful: ${user.id}")
            Result.Success(user)
        } catch (e: Exception) {
            logger.error("AuthRepository", "Sign in failed", e)
            Result.Error(e)
        }
    }
    
    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return Result.Error(Exception("Google Sign In not implemented"))
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            logger.info("AuthRepository", "Signing out")
            authClient.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error("AuthRepository", "Sign out failed", e)
            Result.Error(e)
        }
    }
    
    override suspend fun refreshToken(): Result<String> {
        return try {
            val session = authClient.currentSession()
            if (session != null) {
                Result.Success(session.accessToken)
            } else {
                Result.Error(Exception("No session"))
            }
        } catch (e: Exception) {
            logger.error("AuthRepository", "Refresh token failed", e)
            Result.Error(e)
        }
    }
    
    override suspend fun logout(): Result<Unit> = signOut()
    
    override fun getAuthState(): Flow<AuthState> {
        val currentUser = authClient.currentSession()?.user
        return MutableStateFlow(
            AuthState(
                isAuthenticated = currentUser != null,
                user = currentUser?.let { mapSupabaseUser(it) },
                accessToken = authClient.currentSession()?.accessToken
            )
        )
    }
    
    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val user = authClient.currentSession()?.user
            if (user != null) {
                Result.Success(mapSupabaseUser(user))
            } else {
                Result.Error(Exception("Not authenticated"))
            }
        } catch (e: Exception) {
            logger.error("AuthRepository", "Get current user failed", e)
            Result.Error(e)
        }
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return try {
            authClient.currentSession() != null
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun resetPassword(email: String): Result<Boolean> {
        return try {
            logger.info("AuthRepository", "Sending reset password email")
            authClient.resetPasswordForEmail(email)
            Result.Success(true)
        } catch (e: Exception) {
            logger.error("AuthRepository", "Reset password failed", e)
            Result.Error(e)
        }
    }
    
    private fun mapSupabaseUser(user: SupabaseUser): User {
        return User(
            id = user.id,
            phone = user.phone ?: "",
            email = user.email,
            username = user.userMetadata?.get("username")?.toString() ?: user.email?.substringBefore("@") ?: "",
            displayName = user.userMetadata?.get("display_name")?.toString() ?: user.email?.substringBefore("@"),
            avatarUrl = user.userMetadata?.get("avatar_url")?.toString(),
            bio = user.userMetadata?.get("bio")?.toString(),
            status = UserStatus.ONLINE,
            isVerified = user.emailConfirmedAt != null,
            isPremium = false,
            createdAt = System.currentTimeMillis()
        )
    }
}