package com.nexora.auth.domain.model

data class User(
    val id: String,
    val phone: String,
    val email: String? = null,
    val username: String,
    val displayName: String?,
    val avatarUrl: String?,
    val bio: String?,
    val status: UserStatus = UserStatus.OFFLINE,
    val isVerified: Boolean = false,
    val isPremium: Boolean = false,
    val lastSeen: Long? = null,
    val createdAt: Long
)

enum class UserStatus {
    ONLINE,
    OFFLINE,
    BUSY,
    AWAY
}

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val user: User? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val error: String? = null
)

data class OtpState(
    val phone: String = "",
    val code: String = "",
    val isSent: Boolean = false,
    val isVerified: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val expiresAt: Long = 0
)
