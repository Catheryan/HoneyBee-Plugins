package com.catheryan.analysis

import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.create

class AnalysisPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin(AppPlugin::class.java)) return
        project.extensions.create<RuleExtension>(ANALYSIS_NAME)
        //注册任务名称，感觉没啥吊用
        project.task(ANALYSIS_TASK_NAME)

        //变体组合，方便后续根据变体打印
        val variants = mutableListOf<String>()
        //获取agp-android配置
        val androidComponentsExtension = project.extensions.getByType(AndroidComponentsExtension::class.java)
        //获取android下有多少变体，默认是debug和release版本
        androidComponentsExtension.onVariants { variant ->
            val name = variant.name
            variants.add(name)
            //Options to register asm class visitors and to configure the instrumentation process.
            variant.instrumentation.apply {
                transformClassesWith(
                    AnalysisTransForm::class.java,
                    InstrumentationScope.PROJECT
                ) { params ->
                    params.buildType.set(name)
                }
            }
        }
        //打印对应结果，在任务执行之后
        //配置需要在当前project下的evaluate（评价）之后
        project.afterEvaluate {
            if (variants.isEmpty()) return@afterEvaluate
            val extension = project.properties[ANALYSIS_NAME] as RuleExtension
            //初始化，录入包名路径
            MethodAnalysisUtils.initConfig(extension)
            //打印变体信息
            variants.forEach { variant ->
                //transformDebugClassesWithAsm
                val asmTaskName = "transform${variant.capitalized()}ClassesWithAsm"
                it.tasks.getByName(asmTaskName).doLast {
                    //任务执行之后，生命周期打印映射到的方法以及名称
                    MethodAnalysisUtils.end(variant)
                }
            }
        }
    }
    companion object {
        const val ANALYSIS_NAME = "analysisConfig"
        const val ANALYSIS_TASK_NAME = "analysisCodeTask"
    }
}