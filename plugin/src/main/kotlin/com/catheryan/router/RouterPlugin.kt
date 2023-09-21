package com.catheryan.router

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.gradle.AppPlugin
import com.catheryan.router.ext.RouterDocExt.ROUTER_DOC_DIR
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import java.io.File

/**
 * @author yanzhenghao on 2023/8/31
 * @description
 */
class RouterPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin(AppPlugin::class.java)) return
        with(target){
            extensions.create(ROUTER_EXTENSION,RouterExtension::class.java)
            extensions.getByType(AndroidComponentsExtension::class.java).apply {
                onVariants {
                    val ext = extensions.getByType<RouterExtension>()
                    val registerTask = tasks.register<RouterTask>("${it.name}$ROUTER_TASK_NAME"){
                        enableDoc.set(ext.enableDocs)
                        docOut.set(File("${buildDir}/$ROUTER_DOC_DIR"))
                    }
                    it.artifacts.forScope(ScopedArtifacts.Scope.ALL).use(registerTask)
                        .toTransform(
                            type = ScopedArtifact.CLASSES,
                            inputJars = RouterTask::jars,
                            inputDirectories = RouterTask::dirs,
                            into = RouterTask::output
                        )
                }
            }
        }


    }

    companion object {
        const val ROUTER_EXTENSION = "Router"
        const val ROUTER_TASK_NAME = "RouterTask"
    }
}