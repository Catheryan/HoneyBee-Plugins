plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    /* Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(gradleKotlinDsl())
    implementation(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

//自定义插件名称配置
gradlePlugin {
    plugins {
        create("demo-plugin"){
            id = "com.catheryan.demo"
            implementationClass = "com.catheryan.DemoPlugin"
            displayName = "catheryan gradle plugin demo"
        }
        create("demo-plugin2"){
            id = "com.catheryan.demo2"
            implementationClass = "com.catheryan.DemoPlugin2"
            displayName = "catheryan gradle plugin demo2"
        }
        create("analysisCodePlugin"){
            id = "com.catheryan.analysis.code"
            implementationClass = "com.catheryan.analysis.AnalysisPlugin"
            displayName = "catheryan gradle plugin analysis code for asm"
        }
    }
}
//com.gradle.plugin-publish => maven配置
publishing {
    publications {
        repositories {
            maven {
                name = "CatherRepoMavenLocal"
                url = uri("${rootDir}/repo")
            }
        }
        create<MavenPublication>("CatherRepo"){
            // 配置产物的 JAR 路径
            from(components["java"])
            version = properties["VERSION"].toString()
            artifactId = properties["ARTIFACTID"].toString()
            groupId = properties["GROUPID"].toString()
            description = properties["DESCRIPTION"].toString()
        }
        create<MavenPublication>("CatherRepo2"){
            version = properties["VERSION"].toString()
            artifactId = properties["ARTIFACTID"].toString()
            groupId = properties["GROUPID"].toString()
            description = properties["DESCRIPTION"].toString()
        }
    }

}
