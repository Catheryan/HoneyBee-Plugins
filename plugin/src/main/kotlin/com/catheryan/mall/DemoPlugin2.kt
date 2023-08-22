package com.catheryan.mall

import org.gradle.api.Project
import org.gradle.api.Plugin

class DemoPlugin2: Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register("greeting2222"){
            doLast {
                println("hello world! plugin world22222")
            }
        }
    }
}
