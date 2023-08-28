package com.catheryan.sample

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class CommonConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        with(target){
            pluginManager.withPlugin("java") {
                extensions.configure<JavaPluginExtension> {
                    toolchain {
                        it.languageVersion.set(JavaLanguageVersion.of(17))
                    }
                }
            }

            pluginManager.withPlugin("org.jebrains.kotlin.jvm") {
                extensions.configure<KotlinJvmOptions> {
                    // Treat all Kotlin warnings as errors
                    allWarningsAsErrors = true
                    // Set JVM target to 17
                    jvmTarget = "17"
                    // Allow use of @OptIn
                    freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
                    // Enable default methods in interfaces
                    freeCompilerArgs += "-Xjvm-default=all"
                }
            }
        }
    }
}