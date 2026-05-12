package com.nexora.auth.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexora.auth.presentation.components.*
import com.nexora.auth.presentation.viewmodel.AuthUiState
import com.nexora.shared.theme.NexoraColors

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        onTimeout()
    }
    
    Box(
        modifier = Modifier.fillMaxSize().background(NexoraColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(NexoraColors.Primary, NexoraColors.Secondary)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "NEXORA",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraColors.TextPrimary
            )
            Text(
                text = "CHAT",
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                color = NexoraColors.Primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(
                color = NexoraColors.Primary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun OnboardingScreen(onGetStarted: () -> Unit, onSignIn: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexoraColors.Background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(NexoraColors.Primary, NexoraColors.Secondary)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "NEXORA",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = NexoraColors.TextPrimary
        )
        
        Text(
            text = "CHAT",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            color = NexoraColors.Primary
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        listOf(
            "Instant Messaging" to "Real-time conversations",
            "High-Quality Calls" to "Voice and video calls",
            "Social Feed" to "Share stories and posts",
            "AI Assistant" to "Smart suggestions"
        ).forEach { (title, desc) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(NexoraColors.SurfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (title) {
                            "Instant Messaging" -> Icons.Default.Chat
                            "High-Quality Calls" -> Icons.Default.Call
                            "Social Feed" -> Icons.Default.DynamicFeed
                            else -> Icons.Default.Psychology
                        },
                        contentDescription = null,
                        tint = NexoraColors.Primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, color = NexoraColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = desc, color = NexoraColors.TextSecondary, fontSize = 14.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        NexoraButton(
            text = "Get Started",
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        TextButton(onClick = onSignIn) {
            Text(
                text = "Already have an account? Sign In",
                color = NexoraColors.TextSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun EmailInputScreen(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexoraColors.Background)
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "Back", tint = NexoraColors.TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = if (uiState.isSignUp) "Create Account" else "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = NexoraColors.TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (uiState.isSignUp) "Enter your email to get started" else "Sign in to continue",
            fontSize = 16.sp,
            color = NexoraColors.TextSecondary
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        EmailInput(
            email = uiState.email,
            onEmailChange = onEmailChange,
            isError = uiState.error != null,
            errorMessage = uiState.error
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        NexoraButton(
            text = "Continue",
            onClick = onContinue,
            isLoading = uiState.isLoading,
            enabled = uiState.email.isNotBlank()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun PasswordInputScreen(
    uiState: AuthUiState,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexoraColors.Background)
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "Back", tint = NexoraColors.TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = if (uiState.isSignUp) "Create Password" else "Enter Password",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = NexoraColors.TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = uiState.email,
            fontSize = 14.sp,
            color = NexoraColors.Primary
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        PasswordInput(
            password = uiState.password,
            onPasswordChange = onPasswordChange,
            showRequirements = uiState.isSignUp,
            isError = uiState.error != null,
            errorMessage = uiState.error
        )
        
        if (uiState.isSignUp) {
            Spacer(modifier = Modifier.height(16.dp))
            PasswordInput(
                password = uiState.confirmPassword,
                onPasswordChange = onConfirmPasswordChange,
                label = "Confirm Password",
                placeholder = "Confirm your password"
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        NexoraButton(
            text = if (uiState.isSignUp) "Create Account" else "Sign In",
            onClick = onSubmit,
            isLoading = uiState.isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (!uiState.isSignUp) {
            TextButton(
                onClick = { /* TODO: Reset password */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Forgot Password?", color = NexoraColors.Secondary)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
