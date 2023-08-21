plugins {
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    `java-library-convention`
}

buildscript {
    val kotlinVersion by extra("1.5.31")
    val versionBuildGradle by extra("7.0.3")
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://nexus.bilibili.co/content/groups/carbon/") }
        maven {
            url = uri("${rootDir}/repo")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${versionBuildGradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.catheryan.demo:firstdemo:0.0.1")
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://nexus.bilibili.co/content/groups/carbon/") }
        maven {
            url = uri("${rootDir}/repo")
        }
    }
}