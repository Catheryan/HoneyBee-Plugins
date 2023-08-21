package com.catheryan.mall

import org.gradle.api.Project
import org.gradle.api.Plugin


class DemoPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register("greeting"){
            doLast {
                println("hello world! plugin world")
            }
        }
    }
}
