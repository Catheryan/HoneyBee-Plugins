pluginManagement {
    includeBuild("plugin")
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

//enableFeaturePreview("VERSION_CATALOGS")
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

val samples = buildList{
    val separator = File.separator
    //主要找下sample目录下的项目
    settingsDir.walk()
        .filter { it.name == "build.gradle" || it.name == "build.gradle.kts" }
        .filter { it.path.contains("${separator}samples${separator}") }
        .map { it.parent.substring(rootDir.path.length) }
        .forEach {
            add(it.replace(separator, ":"))
        }
}

// include all available samples and store it in :app project extras.
println("Included samples: $samples")
include(*samples.toTypedArray())
gradle.beforeProject {
    extra["samples"] = samples
}