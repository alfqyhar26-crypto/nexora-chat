plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = ""com.nexora.calls""
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17 ; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = ""17"" }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = ""1.7.0"" }
}

dependencies {
    implementation(project("":core""))
    implementation(project("":shared""))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
