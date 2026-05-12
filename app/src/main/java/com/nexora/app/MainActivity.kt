package com.nexora.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexora.auth.presentation.screens.*
import com.nexora.auth.presentation.viewmodel.AuthStep
import com.nexora.auth.presentation.viewmodel.AuthViewModel
import com.nexora.shared.theme.NexoraColors
import com.nexora.shared.theme.NexoraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            NexoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NexoraColors.Background
                ) {
                    NexoraApp()
                }
            }
        }
    }
}

@Composable
fun NexoraApp(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            AuthNavigation(
                uiState = uiState,
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        
        composable("home") {
            HomeScreen()
        }
    }
}

@Composable
fun AuthNavigation(
    uiState: com.nexora.auth.presentation.viewmodel.AuthUiState,
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit
) {
    LaunchedEffect(uiState.currentStep, uiState.isAuthenticated) {
        if (uiState.currentStep == AuthStep.HOME && uiState.isAuthenticated) {
            onNavigateToHome()
        }
    }
    
    when (uiState.currentStep) {
        AuthStep.SPLASH -> SplashScreen(onTimeout = {})
        
        AuthStep.ONBOARDING -> OnboardingScreen(
            onGetStarted = { viewModel.goToEmailInput(isSignUp = true) },
            onSignIn = { viewModel.goToEmailInput(isSignUp = false) }
        )
        
        AuthStep.EMAIL_INPUT -> EmailInputScreen(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChanged,
            onContinue = viewModel::goToPasswordInput,
            onBack = viewModel::goToOnboarding
        )
        
        AuthStep.PASSWORD_INPUT -> PasswordInputScreen(
            uiState = uiState,
            onPasswordChange = viewModel::onPasswordChanged,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChanged,
            onSubmit = {
                if (uiState.isSignUp) viewModel.signUp() else viewModel.signIn()
            },
            onBack = { viewModel.goToEmailInput(uiState.isSignUp) }
        )
        
        AuthStep.HOME -> {
            // Handled by LaunchedEffect
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to NEXORA",
                color = NexoraColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Chat Platform",
                color = NexoraColors.TextSecondary,
                fontSize = 16.sp
            )
        }
    }
}
