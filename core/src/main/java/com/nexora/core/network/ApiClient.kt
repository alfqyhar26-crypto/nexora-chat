package com.nexora.core.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class ApiClient(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val json: Json
) {
    
    suspend inline fun <reified T> get(endpoint: String): ApiResult<T> {
        return try {
            val response = httpClient.get("$baseUrl$endpoint")
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.value, response.bodyAsText())
            }
        } catch (e: Exception) {
            ApiResult.Error(-1, e.message ?: "Unknown error")
        }
    }
    
    suspend inline fun <reified T, reified R> post(endpoint: String, body: T): ApiResult<R> {
        return try {
            val response = httpClient.post("$baseUrl$endpoint") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.value, response.bodyAsText())
            }
        } catch (e: Exception) {
            ApiResult.Error(-1, e.message ?: "Unknown error")
        }
    }
    
    suspend inline fun <reified T> put(endpoint: String, body: T): ApiResult<T> {
        return try {
            val response = httpClient.put("$baseUrl$endpoint") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.value, response.bodyAsText())
            }
        } catch (e: Exception) {
            ApiResult.Error(-1, e.message ?: "Unknown error")
        }
    }
    
    suspend inline fun <reified T> delete(endpoint: String): ApiResult<T> {
        return try {
            val response = httpClient.delete("$baseUrl$endpoint")
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.value, response.bodyAsText())
            }
        } catch (e: Exception) {
            ApiResult.Error(-1, e.message ?: "Unknown error")
        }
    }
}

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String) : ApiResult<Nothing>()
}