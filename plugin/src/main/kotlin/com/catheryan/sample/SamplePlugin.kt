package com.catheryan.sample

import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.scope.ProjectInfo.Companion.getBaseName
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class SamplePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            val libs = extensions
                .getByType(VersionCatalogsExtension::class.java)
                .named("libs")

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
                apply("com.google.devtools.ksp")
//                apply("dagger.hilt.android.plugin")
                apply<CommonConventionPlugin>()
            }

            pluginManager.withPlugin("java") {
                extensions.configure<JavaPluginExtension> {
                    toolchain {
                        it.languageVersion.set(JavaLanguageVersion.of(17))
                    }
                }
            }

            // TODO: remove when KSP starts respecting the Java/Kotlin toolchain
            tasks.withType(KotlinCompile::class.java).configureEach {
                it.kotlinOptions {
                    jvmTarget = "17"
                }
            }

            pluginManager.withPlugin("com.android.library") {
                configure<LibraryExtension> {
                    compileSdk = 34
                    defaultConfig {
                        minSdk = 21
                        @Suppress("DEPRECATION")
                        targetSdk = 34
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }

                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_17
                        targetCompatibility = JavaVersion.VERSION_17
                    }

                    buildFeatures {
                        compose = false
                    }

//                    composeOptions {
//                        kotlinCompilerExtensionVersion =
//                            libs.findVersion("composeCompiler").get().toString()
//                    }
                }
            }
            println("Library aliases: ${libs.libraryAliases}")
            dependencies {
                // Do not add the shared module to itself
//                if (!project.displayName.contains("samples:base")) {
//                    "implementation"(project(":samples:base"))
//                }

                "implementation"(libs.findLibrary("android.supportV7").get())
//                "implementation"(platform(libs.findLibrary("compose.bom").get()))
//                "androidTestImplementation"(platform(libs.findLibrary("compose.bom").get()))

//                "implementation"(libs.findLibrary("casa.base").get())
//                "ksp"(libs.findLibrary("casa.processor").get())
//
//                "implementation"(libs.findLibrary("hilt.android").get())
//                "kapt"(libs.findLibrary("hilt.compiler").get())

//                "implementation"(libs.findLibrary("androidx.core").get())
//                "implementation"(libs.findLibrary("androidx.fragment").get())
//                "implementation"(libs.findLibrary("androidx.activity.compose").get())
//                "implementation"(libs.findLibrary("compose.foundation.foundation").get())
//                "implementation"(libs.findLibrary("compose.runtime.runtime").get())
//                "implementation"(libs.findLibrary("compose.runtime.livedata").get())
//                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
//                "implementation"(libs.findLibrary("compose.ui.ui").get())
//                "implementation"(libs.findLibrary("compose.material3").get())
//
//                "implementation"(libs.findLibrary("coil.compose").get())
//                "implementation"(libs.findLibrary("coil.video").get())
//
//                "implementation"(libs.findLibrary("accompanist.permissions").get())
//
//                "implementation"(libs.findLibrary("compose.ui.tooling.preview").get())
//                "debugImplementation"(libs.findLibrary("compose.ui.tooling").get())
            }
        }
    }
}