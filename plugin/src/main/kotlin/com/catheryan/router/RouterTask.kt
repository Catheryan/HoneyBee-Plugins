package com.catheryan.router

import com.catheryan.router.ext.RouterInject
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

/**
 * @author yanzhenghao on 2023/8/31
 * @description
 */
abstract class RouterTask : DefaultTask() {

    @get:InputFiles
    abstract val jars: ListProperty<RegularFile>

    @get:Input
    abstract val enableDoc: Property<Boolean>

    @get:InputFiles
    abstract val dirs: ListProperty<Directory>

    @get:OutputDirectory
    abstract val docOut: RegularFileProperty

    @get:OutputFile
    abstract val output: RegularFileProperty

    @TaskAction
    fun taskAction(){
        val isEnableDoc = enableDoc.get()
        val startTime = System.currentTimeMillis()
        val leftSlash = File.separator == "/"
        JarOutputStream(output.asFile.get().outputStream()).use { jarOutput ->
            val mapClassName = mutableListOf<String>()
            //处理文件目录下的class文件，将_RouterMapping_放入class中
            dirs.get().forEach { directory ->
                val directoryPath =
                    if (directory.asFile.absolutePath.endsWith(File.separatorChar)) {
                        directory.asFile.absolutePath
                    } else {
                        directory.asFile.absolutePath + File.separatorChar
                    }
                directory.asFile.walk().asSequence().filter { it.isFile }.forEach dirChild@{ file ->
                    val entryName = if (leftSlash) {
                        file.path.substringAfter(directoryPath)
                    } else {
                        file.path.substringAfter(directoryPath).replace(File.separatorChar, '/')
                    }
                    if (entryName.isEmpty()) return@dirChild
                    if (entryName.startsWith(INJECTED_MAPPING_CLASS_PATH)) {
                        mapClassName.add(entryName)
                    }
                    // copy to
                    file.inputStream().use { input ->
                        jarOutput.saveEntry(entryName, input)
                    }
                }
            }
            var injectByteArray: ByteArray? = null
            //处理Jar中的class文件
            jars.get().forEach { file ->
                JarFile(file.asFile).use { jar ->
                    jar.entries().iterator().asSequence().filter { !it.isDirectory }
                        .filter { it.name.isNotEmpty() }
                        .filter { !it.name.contains("META-INF/") }
                        .forEach jarFile@{ jarEntry ->
                            if (jarEntry.name == INJECTED_NAVIGATION_CLASS_PATH){
                                jar.getInputStream(jarEntry).use {
                                    injectByteArray = it.readAllBytes()
                                }
                                return@jarFile
                            }
                            if (jarEntry.name.startsWith(INJECTED_MAPPING_CLASS_PATH)) {
                                mapClassName.add(jarEntry.name)
                            }
                            // copy to jar
                            jar.getInputStream(jarEntry).use {
                                jarOutput.saveEntry(jarEntry.name, it)
                            }
                        }
                }
            }
            println("Router plugin query mapping spend ${System.currentTimeMillis() - startTime} ms")
            checkNotNull(injectByteArray) {
                println("Make sure you rely on router_core modules?")
            }
            println("Router-> start router mapping inject------>")
            injectByteArray?.takeIf { it.isNotEmpty() }?.let {
                val navigationBytes = RouterInject.inject(it, mapClassName)
                jarOutput.saveEntry(INJECTED_NAVIGATION_CLASS_PATH, ByteArrayInputStream(navigationBytes))
                println("Router-> router mapping inject success------>")
            } ?: kotlin.run {
                println("Router-> router mapping inject failed------>")
            }

        }
    }
    companion object {
        const val INJECTED_MAPPING_CLASS_PATH = "com/catheryan/router/mapping/_RouterMapping_"
        const val INJECTED_NAVIGATION_CLASS_PATH = "com/catheryan/platform/router/router_core/RouterNavigation.class"
    }

}

private fun JarOutputStream.saveEntry(mapName: String, inputStream: InputStream) {
    putNextEntry(JarEntry(mapName))
    inputStream.copyTo(this)
    closeEntry()
}
