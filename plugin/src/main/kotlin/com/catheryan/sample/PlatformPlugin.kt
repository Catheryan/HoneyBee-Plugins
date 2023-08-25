package com.catheryan.sample

import org.gradle.api.Plugin
import org.gradle.api.Project

class PlatformPlugin : Plugin<Project>{

    override fun apply(target: Project) {
        with(target){
            tasks.register("createSample",CreateSamplePlugin::class.java){
                it.projectDir.set(project.projectDir)
            }
        }
    }
}