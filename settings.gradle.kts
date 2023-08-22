pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
//        maven {
//            url = uri("${rootDir}/repo")
//        }
        maven { setUrl("https://nexus.bilibili.co/content/groups/carbon/") }
    }
}

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
//        maven {
//            url = uri("${rootDir}/repo")
//        }
        maven { setUrl("https://nexus.bilibili.co/content/groups/carbon/") }
    }
}
rootProject.name = "demo"
include(":app")
includeBuild("plugin")
