import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)  // Hilt plugin
    alias(libs.plugins.ksp)
//    alias(libs.plugins.kapt)

}

android {
    namespace = "com.example.runescapemarketwatchv3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.runescapemarketwatchv3"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        exclude("META-INF/gradle/incremental.annotation.processors")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit and Gson
    implementation(libs.retrofit)
    implementation(libs.gson)

    // Kotlin Coroutines
    implementation(libs.kotlinCoroutines)

    // Picasso
    implementation(libs.picasso)

    implementation(libs.coil.compose)  // Using the alias from libs.versions.toml

    implementation(libs.koin.android)  // Koin for DI
    implementation(libs.koin.androidx.compose)  // Koin Compose for DI

    implementation(libs.retrofit.gson)  // Retrofit Gson converter
    implementation(libs.androidx.lifecycle.runtime)  // Lifecycle runtime
    implementation(libs.androidx.compose.ui)  // Compose UI
    implementation(libs.androidx.compose.material3)  // Material 3 components
    implementation(libs.androidx.compose.tooling)  // Tooling for Compose

    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit and Gson
    implementation(libs.retrofit)
    implementation(libs.gson)

    // Kotlin Coroutines
    implementation(libs.kotlinCoroutines)

    // Image loading libraries
    implementation(libs.coil.compose)

    // Dependency Injection (Koin)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Picasso (Optional if you're using it)
    implementation(libs.picasso)

    // ViewModel & LiveData
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    // Hilt dependencies
    implementation(libs.hilt.android)  // Hilt Android
    implementation(libs.hilt.compiler)  // Hilt Compiler

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Exposed for Database interaction
    implementation(libs.exposedCore)
    implementation(libs.exposedDao)
    implementation(libs.exposedJdbc)

    // PostgreSQL driver
    implementation(libs.postgresql)

    // Coroutine dependencies for background work
    implementation(libs.coroutinesAndroid)

    implementation(libs.roomRuntime)  // Room database runtime
    implementation(libs.roomKtx)      // Room Kotlin extensions (optional, but useful)

    // Add this if you use KSP (Kotlin Symbol Processor)
    ksp(libs.roomCompiler)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.mpandroidchart)
}