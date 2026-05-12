package com.nexora.app

import android.content.Context
import com.nexora.auth.data.repository.AuthRepositoryImpl
import com.nexora.auth.domain.repository.AuthRepository
import com.nexora.core.network.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.supabase.gotrue.SupabaseAuthClient
import io.supabase.gotrue.ktx.AuthConfig
import io.supabase.gotrue.ktx.auth
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    private const val SUPABASE_URL = "https://exwrgtbzacmfvkdbcmzx.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImV4d3JndGFiY2FjZmd2a2RiY3p4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzg1MTg2MDAsImV4cCI6MjA1NDA5NDYwMH0.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"

    @Provides
    @Singleton
    fun provideSupabaseAuthClient(
        @ApplicationContext context: Context
    ): SupabaseAuthClient {
        return context.auth {
            AuthConfig(
                url = SUPABASE_URL,
                headers = mapOf("apikey" to SUPABASE_ANON_KEY)
            )
        }
    }
    
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
    
    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) { json(json) }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 30_000
        }
        defaultRequest {
            headers.append("Content-Type", "application/json")
            headers.append("apikey", SUPABASE_ANON_KEY)
        }
    }
    
    @Provides
    @Singleton
    fun provideApiClient(httpClient: HttpClient, json: Json): ApiClient {
        return ApiClient(httpClient, SUPABASE_URL, json)
    }
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        authClient: SupabaseAuthClient,
        apiClient: ApiClient
    ): AuthRepository {
        return AuthRepositoryImpl(authClient, apiClient)
    }
}