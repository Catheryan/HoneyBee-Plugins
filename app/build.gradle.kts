@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.catheryan.demo2)
    alias(libs.plugins.catheryan.analysisCode)
}

analysisConfig{
    enableLog = true
    packages = arrayOf("java.io.PrintStream")
}

android {
    namespace = "com.catheryan.demo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.catheryan.demo"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.android.supportV7)
    implementation(libs.android.constraintLayout)
    implementation(libs.android.liveData)
    implementation(libs.android.viewModel)
}