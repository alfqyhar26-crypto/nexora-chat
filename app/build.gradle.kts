plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = ""com.nexora.app""
    compileSdk = 35

    defaultConfig {
        applicationId = ""com.nexora.app""
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = ""1.0.0-alpha""

        testInstrumentationRunner = ""androidx.test.runner.AndroidJUnitRunner""
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", ""SUPABASE_URL"", ""\""https://exwrgtbzacmfvkdbcmzx.supabase.co\"""")
        buildConfigField("String", ""SUPABASE_ANON_KEY"", ""\""eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9\"""")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile(""proguard-android-optimize.txt""),
                ""proguard-rules.pro""
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = "".debug""
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = ""17""
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ""1.7.0""
    }

    packaging {
        resources {
            excludes += ""/META-INF/{AL2.0,LGPL2.1}""
        }
    }
}

dependencies {
    implementation(project("":core""))
    implementation(project("":shared""))
    implementation(project("":features:auth""))
    implementation(project("":features:chat""))
    implementation(project("":features:calls""))
    implementation(project("":features:stories""))
    implementation(project("":features:explore""))
    implementation(project("":features:profile""))
    implementation(project("":features:settings""))

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Navigation
    implementation(libs.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // DataStore
    implementation(libs.datastore.preferences)

    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
}
