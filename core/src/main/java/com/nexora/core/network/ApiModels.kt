package com.nexora.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
    val message: String? = null
)

@Serializable
data class PaginatedResponse<T>(
    val data: List<T>,
    val page: Int,
    val limit: Int,
    val total: Int,
    val hasMore: Boolean
)

sealed class ApiException(message: String, val code: Int = 0) : Exception(message) {
    data object NetworkError : ApiException("Network connection error", 0)
    data object TimeoutError : ApiException("Request timeout", 408)
    data class ServerError(val msg: String) : ApiException(msg, 500)
    data class Unauthorized(val msg: String = "Unauthorized") : ApiException(msg, 401)
    data class NotFound(val msg: String = "Resource not found") : ApiException(msg, 404)
    data class ValidationError(val msg: String) : ApiException(msg, 400)
    data class Unknown(val msg: String) : ApiException(msg, -1)
}
