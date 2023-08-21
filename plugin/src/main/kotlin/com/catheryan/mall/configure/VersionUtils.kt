package com.catheryan.mall.configure

import org.gradle.api.Project
import org.yaml.snakeyaml.Yaml
import java.io.File


internal fun Project.getModuleInfo(): HashMap<String, ModuleEntity> {
    return this.parserModule(File(rootProject.rootDir,"../.module.yaml"))
}

fun Project.parserModule(file: File): HashMap<String, ModuleEntity> {
    val yaml = Yaml()
    val module = yaml.load<HashMap<String, List<HashMap<String, Any>>>>(
        file.getModuleYamlFile().inputStream()
    )

    val moduleList = hashMapOf<String, ModuleEntity>()
    logger.lifecycle(module["module"]?.toString())
    module["module"]?.forEach {
        val appVersionName = it["APP_VERSION_NAME"]?.toString()
            ?: throw  java.lang.RuntimeException(".module.yaml $it has empty APP_VERSION_NAME")
        val appVersionCode = it["APP_VERSION_CODE"]?.toString()
            ?: throw  java.lang.RuntimeException(".module.yaml $it has empty APP_VERSION_CODE")
        val appkey = it["APP_KEY"]?.toString()
            ?: throw  java.lang.RuntimeException(".module.yaml $it has empty APP_KEY")
        moduleList[appkey] = ModuleEntity(appVersionName, appVersionCode)
    }
    return moduleList
}

fun File.getModuleYamlFile(): File {
    val moduleYaml = File(this, ".module.yaml")
    return if (moduleYaml.exists()) {
        moduleYaml
    } else {
        val parent: File? = parentFile
        parent?.getModuleYamlFile() ?: throw  RuntimeException("没有找到.module.yaml")
    }
}

data class ModuleEntity(val appVersionName: String, val appVersionCode: String)
