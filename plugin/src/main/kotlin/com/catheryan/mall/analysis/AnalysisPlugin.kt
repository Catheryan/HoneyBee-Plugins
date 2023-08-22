package com.catheryan.mall.analysis

import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
class AnalysisPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin(AppPlugin::class.java)) return
    }
}