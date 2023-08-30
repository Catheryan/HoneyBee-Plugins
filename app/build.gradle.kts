@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
//    id("com.catheryan.analysis.code")
}
//
//analysisConfig{
//    enableLog = true
//    packages = arrayOf("java.io.PrintStream")
//}

java {
    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
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

    viewBinding {
        enable = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kapt {
        useBuildCache = true
        arguments {

        }
    }
}

dependencies {
    implementation(libs.android.liveData)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment)
    implementation(libs.android.viewModel)
    // include all available samples.
    val samples: List<String> by project.extra
    samples.forEach {
        implementation(project(it))
    }
    kapt(project(":samples:router:router_apt"))
}