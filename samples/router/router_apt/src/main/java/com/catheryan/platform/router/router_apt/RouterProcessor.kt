
/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.catheryan.platform.router.router_apt

import com.catheryan.platform.router.router_annotation.RouterAnnotation
import com.catheryan.platform.router.router_apt.data.RouterData
import com.google.auto.service.AutoService
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.Math.abs
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedAnnotationTypes("com.catheryan.platform.router.router_annotation.RouterAnnotation")
class RouterProcessor : BaseProcessor() {
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (roundEnv.processingOver() || annotations.isEmpty()) return false
        logger.info("-------> start -----")
        generateRouterMappings(roundEnv)
        logger.info("------->  end  -----")
        return true
    }

    private fun generateRouterMappings(roundEnvironment: RoundEnvironment) {
        val elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(RouterAnnotation::class.java)
        if (elementsAnnotatedWith.size < 1) return
        logger.info("-------> 收集到 ${elementsAnnotatedWith.size} 个使用 [RouterAnnotation] 的类-----")
        val mapping = mutableMapOf<String, RouterData>()
        val docJsons = JsonArray()
        elementsAnnotatedWith.forEach {
            (it as? TypeElement)?.let { element ->
                // 获取注解信息
                element.getAnnotation(RouterAnnotation::class.java)?.apply {
                    val realPath = element.qualifiedName.toString()
                    mapping[url] = RouterData(url, realPath, desc)
                    val json = JsonObject().apply {
                        addProperty("url", url)
                        addProperty("cla", realPath)
                        addProperty("desc", desc)
                    }
                    docJsons.add(json)
                    logger.info("-------> url:$url, className:$realPath -----")
                }
            }
        }
        writeMappingClass(mapping, docJsons)
    }

    private fun writeMappingClass(mapping: MutableMap<String, RouterData>, docJsons: JsonArray) {
        if (mapping.isEmpty()) return
        val createSourceFile = processingEnv.filer.createSourceFile("$PACKAGE$POINT${PREFIX_ROUTER_MAP}temp")
        val path = createSourceFile.toUri().path
        val className = "$PREFIX_ROUTER_MAP${abs(path.hashCode())}"
        logger.info("-------> mappingClassName:$className -----")
        val source = processingEnv.filer.createSourceFile(className)
        val writeMappingClassContent = getMappingClassContent(className, mapping, docJsons)
        source.openWriter().use {
            it.write(writeMappingClassContent)
        }
    }

    private fun getMappingClassContent(
        className: String,
        map: Map<String, RouterData>,
        docs: JsonArray
    ): String {
        val builder = StringBuilder()
        builder.append("package ${PACKAGE};\n")
        builder.append("import java.util.Map;\n")
        builder.append(
            "\n/**\n" +
                    " * 自动生成的路由表\n" +
                    " * @author catheryan\n" +
                    " */\n",
        )
            .append("public class ").append(className)
            .append(" implements com.catheryan.platform.router.router_core._IRouter ").append(" {\n\n")
            .append(
                String.format(
                    "    private final String params = \"%s\";\n\n",
                    docs.toString().replace("\"", "\\\"")
                )
            )
            .append("    @Override\n")
            .append("    public void init(Map<String, com.catheryan.platform.router.router_apt.data.RouterData> map) {\n")
        if (map.isNotEmpty()) {
            map.forEach { (url, data) ->
                builder.append("        map.put(\"$url\", new com.catheryan.platform.router.router_apt.data.RouterData(\"${data.url}\", \"${data.cla}\", \"${data.desc}\"));\n")
            }
        }
        builder.append("    }\n")
        builder.append("}")
        return builder.toString()
    }

    companion object {
        private const val POINT = "."
        private const val PREFIX_ROUTER_MAP = "_RouterMapping_"
        private const val PACKAGE = "com.catheryan.router.mapping"
    }
}