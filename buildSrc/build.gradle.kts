
plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {

    maven {
        url = uri("${rootDir}/repo")
    }
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven { setUrl("https://nexus.bilibili.co/content/groups/carbon/") }
}
val versionBuildGradle by extra("7.0.3")
dependencies {
    implementation("com.bmuschko:gradle-docker-plugin:6.4.0")
}
