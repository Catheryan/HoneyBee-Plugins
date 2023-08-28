package com.catheryan

import org.gradle.api.Project
import org.gradle.api.Plugin

class DemoPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register("greeting") {
                it.doLast {
                    println("hello world! plugin world")
                }
            }
        }
    }
}
